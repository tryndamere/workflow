package com.rocky.workflow;

import com.rocky.workflow.exception.WorkFlowRuntimeException;

/**
 * Created by rocky on 14-4-6.
 */
public interface WorkFlowForm {

    /**
     * 工具条初始化数据
     * @param taskId 当前环节实例ID task==null代表是起草环节
     * @param processDefinitionKey 当taskId为空时，此值必须有
     * @return
     */
    public String tools(String taskId , String processDefinitionKey) throws WorkFlowRuntimeException;
    
    /**
     * 查看下一环节人员
     * @param taskId 当前环节实例ID
     * @param processDefinitionKey 当taskId为空时，此值必须有
     * @return 
     * @throws WorkFlowRuntimeException
     */
    public String nextTask(String taskId, String processDefinitionKey, String paramJson) throws WorkFlowRuntimeException;

    /**
     * 查看流程意见内容
     * @param taskId 当前环节实例ID
     * @return
     * @throws WorkFlowRuntimeException
     */
    public String comments(String taskId) throws WorkFlowRuntimeException;
    
}
