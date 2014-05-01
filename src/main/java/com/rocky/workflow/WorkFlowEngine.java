package com.rocky.workflow;

/**
 * Created by rocky on 14-4-6.
 */
public interface WorkFlowEngine {

    public WorkFlowRuntime getWorkFlowRuntime();

    public WorkFlowForm getWorkFlowForm();

    public WorkFlowTrace getWorkFlowTrace();

    public WorkFlowQueryTask getWorkFlowQueryTask();

}
