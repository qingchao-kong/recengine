package com.qingchao.recengine.core;

import com.alibaba.fastjson.JSON;
import com.dianping.cat.Cat;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.loginfo.LogInfo;
import com.qingchao.recengine.core.bean.request.DefaultRecRequest;
import com.qingchao.recengine.core.bean.response.DefaultRecResponse;
import com.qingchao.recengine.core.config.RecEngineConfig;
import com.qingchao.recengine.core.enums.StageEnum;
import com.qingchao.recengine.core.util.ConfigUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.qingchao.recengine.core.constant.ConfigConst.*;

/**
 * 推荐引擎主类；⚠️整个服务生命周期，每个场景实例化一个RecEngine，只实例化一次⚠️；
 *
 * @author kongqingchao
 */
@Slf4j
public final class RecEngine<PC extends AbstractRecPluginContext, RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> {
    /**
     * 推荐引擎对应的场景
     */
    private final String scene;

    /**
     * 推荐引擎线程池，用于保证推荐逻辑和召回逻辑的超时控制；
     */
    private final ThreadPoolExecutor poolExecutor;

    /**
     * 推荐引擎插件上下文；⚠️单例⚠️
     */
    private final PC pluginContext;

    /**
     * 请求上下文的实现类，用于实例化请求上下文
     */
    private final Class<RC> requestContextClass;

    /**
     * 物品实现类
     */
    private final Class<C> candidateClass;

    /**
     * 本地缓存的默认推荐配置，用于拉取配置中心配置失败时使用
     */
    private RecEngineConfig defaultEngineConfig;

    /**
     * 本地缓存的兜底配置，用于正常流程失败时使用
     */
    private RecEngineConfig compensateEngineConfig;

//    @Setter
//    private boolean mock;

    public RecEngine(String scene, ThreadPoolExecutor poolExecutor, PC pluginContext, Class<RC> requestContextClass, Class<C> candidateClass) {
        this.scene = scene;
        //设置RecEngine里线程名格式
        final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("RecEngine-" + scene + "-%d").build();
        poolExecutor.setThreadFactory(threadFactory);
        this.poolExecutor = poolExecutor;
        this.pluginContext = pluginContext;
        this.requestContextClass = requestContextClass;
        this.candidateClass = candidateClass;
    }

    /**
     * 请求处理入口
     *
     * @param recRequest  可以为AbstractRecRequest的子类,推荐流请求体
     * @param recResponse 可以为AbstractRecResponse的子类,推荐流响应
     * @return 执行是否成功
     * @throws Exception 执行异常
     */
    public boolean process(DefaultRecRequest recRequest, DefaultRecResponse recResponse) throws Exception {
        long start = System.currentTimeMillis();
        boolean succ = processRequest(recRequest, recResponse);
        LogInfo.builder()
                .explainLog(true)
                .requestId(recRequest.getRequestId())
                .uid(recRequest.getUid())
                .scene(this.scene)
                .reqParams(JSON.toJSONString(recRequest))
                .startTime(start)
                .costTime(System.currentTimeMillis() - start)
                .stage(StageEnum.requestProcess)
                .success(succ)
                .result(JSON.toJSONString(recResponse))
                .build()
                .log();
        return succ;
    }

    private boolean processRequest(DefaultRecRequest recRequest, DefaultRecResponse recResponse) throws Exception {
        //get config
        final RecEngineConfig recEngineConfig = getConfig(recRequest);

        //1.正常流程
        if (ConfigUtil.checkRecEngineConfig(recEngineConfig)) {
            final Future<Boolean> contextFuture = submitContextCallable(new ContextCallable(this.scene, recEngineConfig, recRequest,
                    recResponse, this.pluginContext, this.requestContextClass, this.candidateClass, this.poolExecutor, true));
            if (Objects.nonNull(contextFuture)) {
                try {
                    final Boolean res = contextFuture.get(recEngineConfig.getExtParameter().getTimeout(), TimeUnit.MILLISECONDS);
                    if (res) {
                        return true;
                    }
                } catch (Throwable throwable) {
                    final String format = String.format("RecEngine, waiting context callable result error, error:%s, scene:%s, request:%s", throwable, this.scene, recRequest);
                    log.error(format, throwable);
                    Cat.logError(format, throwable);
                }
            }
        }

        //2.正常流程失败，检查是否走兜底流程：
        if (recEngineConfig.getExtParameter().getCompensate()
                && ConfigUtil.checkRecEngineConfig(compensateEngineConfig)) {
            final Future<Boolean> compensateCtxFuture = submitContextCallable(new ContextCallable(this.scene, this.compensateEngineConfig, recRequest,
                    recResponse, this.pluginContext, this.requestContextClass, this.candidateClass, this.poolExecutor, false));
            if (Objects.nonNull(compensateCtxFuture)) {
                try {
                    final Boolean compensateRes = compensateCtxFuture.get(this.compensateEngineConfig.getExtParameter().getTimeout(), TimeUnit.MILLISECONDS);
                    if (compensateRes) {
                        return true;
                    }
                } catch (Throwable throwable) {
                    final String format = String.format("RecEngine, waiting compensate context callable result error, error:%s, scene:%s, request:%s", throwable, this.scene, recRequest);
                    log.error(format, throwable);
                    Cat.logError(format, throwable);
                }
            }
        }

        //3.正常流程、兜底流程都未返回结果，返回false
        return false;
    }

    private Future<Boolean> submitContextCallable(ContextCallable callable) {
        try {
            return this.poolExecutor.submit(callable);
        } catch (Throwable throwable) {
            final String format = String.format("RecEngine. Submit context callable error, scene:%s, error:%s", this.scene, throwable);
            Cat.logError(format, throwable);
            log.error(format, throwable);
            return null;
        }
    }

    private RecEngineConfig getConfig(DefaultRecRequest recRequest) {
        RecEngineConfig recEngineConfig;
        Map<String, RecEngineConfig> mapConfig;
//        if (this.mock) {
//        mapConfig = this.pluginContext.mockConfig(recRequest, this.scene);
//        } else {
        mapConfig = this.pluginContext.getConfig(recRequest, this.scene);
//        }
        if (Objects.nonNull(mapConfig)) {
            recEngineConfig = mapConfig.get(RESULT);
            this.defaultEngineConfig = mapConfig.get(DEFAULT);
            this.compensateEngineConfig = mapConfig.get(COMPENSATE);
        } else {
            recEngineConfig = this.defaultEngineConfig;
        }
        return recEngineConfig;
    }

}
