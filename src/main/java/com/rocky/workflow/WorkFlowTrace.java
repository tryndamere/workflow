package com.rocky.workflow;

import java.io.InputStream;

/**
 * 流程跟踪接口
 * @author rocky
 */
public interface WorkFlowTrace {

    /**
     * 流程跟踪——文本
     * @param taskId 当前环节实例ID
     * @return
     */
    public String traceText(String taskId) ;

    /**
     * 流程跟踪——图片
     * @param taskId 当前环节实例ID
     * @return
     */
    public InputStream traceImage(String taskId , String key);

    /**
     * 流程跟踪——图片文本
     * @param taskId 当前环节实例ID
     * @return
     */
    public String traceText2Image(String taskId);

}
