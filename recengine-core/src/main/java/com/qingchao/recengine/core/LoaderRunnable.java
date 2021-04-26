package com.qingchao.recengine.core;

import com.alibaba.fastjson.JSON;
import com.dianping.cat.Cat;
import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.loginfo.LogInfo;
import com.qingchao.recengine.core.enums.StageEnum;
import com.qingchao.recengine.core.plugin.loader.AbstractLoader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * 描述:召回执行
 *
 * @author kongqingchao
 * @create 2021-03-11 4:41 下午
 */
@AllArgsConstructor
@Getter
@Slf4j
public class LoaderRunnable<C extends AbstractRecCandidate> implements Runnable {
    private String scene;
    private AbstractLoader loader;
    private CountDownLatch latch;
    private List<C> loadedCandidates;
    private AbstractRecRequestContext requestContext;

    private boolean normalFlow;

    @Override
    public void run() {
        final long start = System.currentTimeMillis();
        boolean succ = false;
        List<String> idList = new ArrayList<>();
        Throwable err = null;
        String msg = null;
        try {
            succ = loader.load(this.requestContext, loadedCandidates);
            if (succ) {
                idList = loadedCandidates.stream().map(AbstractRecCandidate::getId).collect(Collectors.toList());
            }
        } catch (Throwable throwable) {
            msg = String.format("RecEngine. LoaderRunnable execute error, error:%s, scene:%s", throwable, this.scene);
            Cat.logError(msg, throwable);
            log.error(msg, throwable);
            err = throwable;
        } finally {
            this.latch.countDown();
            LogInfo.builder()
                    .explainLog(true)
                    .normalFlow(requestContext.isNormalFlow())
                    .requestId(requestContext.getRecRequest().getRequestId())
                    .uid(requestContext.getRecRequest().getUid())
                    .scene(requestContext.getRecEngineConfig().getScene())
                    .startTime(start)
                    .costTime(System.currentTimeMillis() - start)
                    .stage(StageEnum.oneLoader)
                    .pluginName(loader.getName())
                    .success(succ)
                    .result(JSON.toJSONString(idList))
                    .throwable(err)
                    .msg(msg)
                    .build()
                    .log();
        }
    }
}
