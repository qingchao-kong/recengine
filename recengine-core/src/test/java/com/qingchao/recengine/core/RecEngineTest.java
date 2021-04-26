package com.qingchao.recengine.core;


import com.qingchao.recengine.core.bean.candidate.AbstractRecCandidate;
import com.qingchao.recengine.core.bean.context.AbstractRecPluginContext;
import com.qingchao.recengine.core.bean.context.AbstractRecRequestContext;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-02 7:30 下午
 */
public class RecEngineTest {
    private static final ThreadPoolExecutor poolExecutor =
            new ThreadPoolExecutor(40, 200,
                    60L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(20), new ThreadPoolExecutor.CallerRunsPolicy());

    @Test
    public void process_Test() {
        //RecEngine 每个场景服务启动时实例化一次；
        AbstractRecPluginContext pluginContext = new AbstractRecPluginContext();
        RecEngine engine = new RecEngine("test", poolExecutor, pluginContext, AbstractRecRequestContext.class, AbstractRecCandidate.class);

//        RecEngine<AbstractRecPluginContext, AbstractRecRequestContext> recEngine=new RecEngine<>(poolExecutor,500,new AbstractRecPluginContext(),AbstractRecRequestContext.class);

//        DefaultRecRequest recRequest = new DefaultRecRequest();
//        DefaultRecResponse recResponse = new DefaultRecResponse();
//
//        try {
//            final boolean process = engine.process(recRequest, recResponse);
//            Assert.assertTrue(process);
//        } catch (Throwable throwable) {
//            //xxx
//        }
    }
}
