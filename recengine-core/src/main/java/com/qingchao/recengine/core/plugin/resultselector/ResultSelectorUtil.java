package com.qingchao.recengine.core.plugin.resultselector;

import com.alibaba.fastjson.JSON;
import com.dianping.cat.Cat;
import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import com.qingchao.recengine.core.bean.loginfo.LogInfo;
import com.qingchao.recengine.core.enums.StageEnum;
import com.qingchao.recengine.core.plugin.resultselector.resultfilter.AbstractResultFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-04-23 5:37 下午
 */
@Slf4j
public class ResultSelectorUtil {

    public static <RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> boolean resultSelect(RC requestContext, AbstractResultSelector resultSelector, List<C> input, List<C> output) {
        if (Objects.isNull(resultSelector)) {
            output.addAll(input);
            return true;
        }

        long start = System.currentTimeMillis();
        boolean succ = false;
        String msg = null;
        Throwable err = null;
        List<String> idList = new ArrayList<>();
        try {
            succ = doSelect(requestContext, resultSelector, input, output);
            if (succ) {
                idList = output.stream().map(AbstractRecCandidate::getId).collect(Collectors.toList());
            }
        } catch (Throwable throwable) {
            msg = String.format("RecEngine. ResultSelector error, error:%s", throwable);
            err = throwable;
            log.error(msg, throwable);
            Cat.logError(msg, throwable);
        }
        LogInfo.builder()
                .explainLog(true)
                .normalFlow(requestContext.isNormalFlow())
                .requestId(requestContext.getRecRequest().getRequestId())
                .uid(requestContext.getRecRequest().getUid())
                .scene(requestContext.getRecEngineConfig().getScene())
                .startTime(start)
                .costTime(System.currentTimeMillis() - start)
                .stage(StageEnum.resultSelector)
                .success(succ)
                .result(JSON.toJSONString(idList))
                .msg(msg)
                .throwable(err)
                .build()
                .log();
        return succ;
    }

    /**
     * result merge，RecEngine调用的方法；
     *
     * @param requestContext
     * @param input
     * @return
     */
    public static <RC extends AbstractRecRequestContext, C extends AbstractRecCandidate> boolean doSelect(RC requestContext, AbstractResultSelector resultSelector, List<C> input, List<C> output) {
        //1.遍历input.size次数，调select,一次一个，保存到map，将list和map都传入select，避免重复导致死循环
        //2.遍历input调allFilters，如果数量满足，return，如果数量不满足，执行3
        //3.遍历map，调mustfilters，
        Set<String> viewedIdSet = new HashSet<>();
        for (int num = 0; num < input.size(); num++) {

            final C selected = (C) resultSelector.select(requestContext, input, viewedIdSet, num);
            if (Objects.nonNull(selected)) {
                viewedIdSet.add(selected.getId());
                boolean ok = true;
                for (Object o : resultSelector.getMustFilters()) {
                    AbstractResultFilter resultFilter = (AbstractResultFilter) o;
                    ok = resultFilter.filter(requestContext, selected, output);
                    if (!ok) {
                        break;
                    }
                }
                if (ok) {
                    for (Object o : resultSelector.getShouldFilters()) {
                        AbstractResultFilter resultFilter = (AbstractResultFilter) o;
                        ok = resultFilter.filter(requestContext, selected, output);
                        if (!ok) {
                            break;
                        }
                    }
                    if (ok) {
                        output.add(selected);
                    }
                }
            }
            if (output.size() >= resultSelector.getSize()) {
                return true;
            }
        }

        viewedIdSet.clear();
        output.clear();

        for (int num = 0; num < input.size(); num++) {
            final C selected = (C) resultSelector.select(requestContext, input, viewedIdSet, num);
            if (Objects.nonNull(selected)) {
                viewedIdSet.add(selected.getId());
                boolean ok = true;
                for (Object o : resultSelector.getShouldFilters()) {
                    AbstractResultFilter resultFilter = (AbstractResultFilter) o;
                    ok = resultFilter.filter(requestContext, selected, output);
                    if (!ok) {
                        break;
                    }
                }
                if (ok) {
                    output.add(selected);
                }
            }
            if (output.size() >= resultSelector.getSize()) {
                return true;
            }
        }

        if (output.size() > 0) {
            return true;
        } else {
            return false;
        }

    }

//        for (num = 0; num < input.size(); num++) {
//            C select = select(input, map, result, index, num);
//            map.put(select.getId, select);
//            if (Objects.nonNull(select)) {
//                boolean f = true;
//                for (Filter filter : filters) {
//                    f = filter.filter(select, result);
//                    if (!f) {
//                        break;
//                    }
//                }
//                if (f) {
//                    result.add(select);
//                }
//            }
//            if (result.size() >= size) {
//                return true;
//            }
//        }
//
//        for (num = 0; num < input.size(); num++) {
//            C select = select(input, map, result, index, num);
//            map.put(select.getId, select);
//            if (Objects.nonNull(select)) {
//                boolean f = true;
//                for (Filter filter : filters) {
//                    if (filter.getMust()) {
//                        f = filter.filter(select, result);
//                        if (!f) {
//                            break;
//                        }
//                    }
//                }
//                if (f) {
//                    result.add(select);
//                }
//            }
//            if (result.size() >= size) {
//                return true;
//            }
//        }
//
//        return true;
}
