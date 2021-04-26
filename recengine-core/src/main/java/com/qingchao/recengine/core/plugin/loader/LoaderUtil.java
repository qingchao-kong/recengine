package com.qingchao.recengine.core.plugin.loader;

import com.dianping.cat.Cat;
import com.qingchao.recengine.core.LoaderRunnable;
import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.loginfo.LogInfo;
import com.qingchao.recengine.core.enums.StageEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-25 11:52 上午
 */
@Slf4j
public class LoaderUtil {
    public static <C extends AbstractRecCandidate> boolean multiLoad(List<AbstractLoader> loaders, AbstractRecRequestContext requestContext, Map<String, List<C>> allLoadedCandidates, ThreadPoolExecutor poolExecutor) {
        long start = System.currentTimeMillis();
        boolean succ = doAllLoad(loaders, requestContext, allLoadedCandidates, poolExecutor);
        LogInfo.builder()
                .explainLog(true)
                .normalFlow(requestContext.isNormalFlow())
                .requestId(requestContext.getRecRequest().getRequestId())
                .uid(requestContext.getRecRequest().getUid())
                .scene(requestContext.getRecEngineConfig().getScene())
                .startTime(start)
                .costTime(System.currentTimeMillis() - start)
                .stage(StageEnum.loaderStage)
                .success(succ)
                .build()
                .log();
        return succ;
    }

    public static <C extends AbstractRecCandidate> boolean doAllLoad(List<AbstractLoader> loaders, AbstractRecRequestContext requestContext, Map<String, List<C>> allLoadedCandidates, ThreadPoolExecutor poolExecutor) {
        final String scene = requestContext.getRecEngineConfig().getScene();
        //并发执行所有loader
        CountDownLatch latch = new CountDownLatch(loaders.size());
        for (AbstractLoader loader : loaders) {
            try {
                List<C> loadedCandidates = new ArrayList<>();
                allLoadedCandidates.put(loader.getName(),loadedCandidates);
                final LoaderRunnable loaderRunnable = new LoaderRunnable(scene, loader, latch, loadedCandidates, requestContext, requestContext.isNormalFlow());
                poolExecutor.execute(loaderRunnable);
            } catch (Throwable throwable) {
                //countDown
                latch.countDown();
                final String format = String.format("RecEngine. Submit loaderRunnable error, scene:%s, error:%s", scene, throwable);
                Cat.logError(format, throwable);
                log.error(format, throwable);
            }
        }
        try {
            final boolean loadRes = latch.await(requestContext.getRecEngineConfig().getExtParameter().getLoaderTimeout(), TimeUnit.MILLISECONDS);
            if (!loadRes) {
                Map<String, List<String>> idList = getIdList(allLoadedCandidates);
                final String format = String.format("RecEngine. Multi load timeout, scene:%s, loaders:%s, allLoadedCandidates:%s", scene, loaders, idList);
                log.error(format);
                Cat.logError(format, new Exception(format));
            }
        } catch (Throwable throwable) {
            Map<String, List<String>> idList = getIdList(allLoadedCandidates);
            final String format = String.format("RecEngine. Multi load error, error;%s, scene:%s, loaders:%s, allLoadedCandidates:%s", throwable, scene, loaders, idList);
            log.error(format, throwable);
            Cat.logError(format, throwable);
            return false;
        }
        final int allCandNum = allLoadedCandidates.values().stream().mapToInt(List::size).sum();
        if (allCandNum == 0) {
            //所有loader返回总数为0，则失败
            return false;
        }
        return true;
    }

    private static <C extends AbstractRecCandidate> Map<String, List<String>> getIdList(Map<String, List<C>> allLoadedCandidates) {
        Map<String, List<String>> idList = new HashMap<>();

        try {
            allLoadedCandidates.forEach((k, v) -> {
                List<String> collect = v.stream()
                        .map(AbstractRecCandidate::getId)
                        .collect(Collectors.toList());
                idList.put(k, collect);
            });
        } catch (Throwable throwable) {
            String format = String.format("RecEngine. Convert allLoadedCandidates to idList error, error:%s", throwable);
            Cat.logError(format, throwable);
            log.error(format, throwable);
        }
        return idList;
    }
}
