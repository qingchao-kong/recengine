package com.qingchao.recengine.core.bean.loginfo;

import com.alibaba.fastjson.JSON;
import com.dianping.cat.Cat;
import com.qingchao.recengine.core.enums.StageEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;


/**
 * 描述:
 *
 * @author kongqingchao
 * @create 2021-03-24 11:44 上午
 */
@Builder
@Data
@Slf4j
public class LogInfo {
    /**
     * 是否开启日志解释功能
     */
    private Boolean explainLog;
    /**
     * 正常流程/兜底流程
     */
    private Boolean normalFlow;

    private String requestId;

    private Long uid;
    /**
     * 请求场景
     */
    private String scene;
    /**
     * 请求参数
     */
    private String reqParams;
    /**
     * 开始的时间
     */
    private Long startTime;
    /**
     * 所花费的时间
     */
    private Long costTime;
    /**
     * 在哪个处理阶段
     */
    private StageEnum stage;
    /**
     * stage的具体哪个实例
     */
    private String pluginName;
    /**
     * 此阶段是否成功
     */
    private Boolean success;
    /**
     * 该次请求的结果
     */
    private String result;
    /**
     * 异常信息
     */
    private Throwable throwable;
    /**
     * 正常/异常信息
     */
    private String msg;

    private String searchKey;
    private String searchScene;
    private String searchUid;

    @Tolerate
    public LogInfo() {
    }

    public void log() {
        if (Objects.isNull(this.requestId)) {
            String format = String.format("RecEngine. Please set logInfo requestTime and requestId. scene:%s, stage:%s", this.scene, this.stage);
            log.error(format);
            Cat.logError(format, new Exception(format));
        }
        this.searchKey = "RecEngineRequestTrace";
        this.searchScene = "searchScene" + this.scene;
        this.searchUid = "searchUid" + this.uid;
        log.info(JSON.toJSONString(this));
    }
}
