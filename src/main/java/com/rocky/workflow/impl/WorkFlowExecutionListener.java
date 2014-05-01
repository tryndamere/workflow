package com.rocky.workflow.impl;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.alibaba.fastjson.JSON;
import com.rocky.workflow.WorkFlowRemote;
import com.rocky.workflow.WorkFlowRuntime.PROCESS_GLOBAL_STATE;
import com.rocky.workflow.util.SpringUtils;

/**
 * 所有事件处理都需要通过此类来实现
 * @author rocky
 */
public class WorkFlowExecutionListener implements ExecutionListener {

  private static final long serialVersionUID = 1L;
  
  private WorkFlowRemote getWorkFlowRemote(){
    return (WorkFlowRemote)SpringUtils.getBean("workFlowRemote");
  }
  
  private RiskInfoService getRiskInfoService(){
    return (RiskInfoService)SpringUtils.getBean("riskInfoService");
  }

  @Override
  public void notify(DelegateExecution delegateExecution) throws Exception {
    Map<String , Object> returnMap = new HashMap<String, Object>();
    Map<String , String> processMap = new HashMap<String, String>();
    processMap.put("processId", delegateExecution.getProcessInstanceId());
    processMap.put("processKey", delegateExecution.getProcessDefinitionId());
    processMap.put("dataId", delegateExecution.getProcessBusinessKey());
    returnMap.put("process", processMap);
    returnMap.put("business", delegateExecution.getVariables());
    getWorkFlowRemote().remote2Client(JSON.toJSONString(returnMap));
    updateBusiData(delegateExecution);
  }
  
  private void updateBusiData(DelegateExecution delegateExecution){
    Map<String,Object> params = new HashMap<String,Object>();
    params.put("busiType", delegateExecution.getVariable("busiType").toString());
    params.put("status", PROCESS_GLOBAL_STATE.CHECK.toString());
    params.put("pid", delegateExecution.getProcessBusinessKey());
    getRiskInfoService().updateRiskStatus(params);
  }

}
