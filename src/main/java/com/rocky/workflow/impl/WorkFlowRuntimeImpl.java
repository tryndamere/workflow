package com.rocky.workflow.impl;

import com.rocky.workflow.WorkFlowRuntime;
import com.rocky.workflow.exception.WorkFlowRuntimeException;
import com.rocky.workflow.impl.db.WorkFlowConfigEntity;
import com.rocky.workflow.impl.json.input.WORK_FLOW_COMPLETE_ENUM;
import com.rocky.workflow.util.CollectionUtils;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * Created by rocky on 14-4-7.
 */
public class WorkFlowRuntimeImpl extends WorkFlowBaseEngine implements WorkFlowRuntime {
  
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkFlowRuntimeImpl.class);

    /**
     * 流程配置对象
     */
    private WorkFlowConfiguration workFlowConfiguration;

    /**
     * 注入流程配置对象
     * @param workFlowConfiguration
     */
    public void setWorkFlowConfiguration(WorkFlowConfiguration workFlowConfiguration) {
        this.workFlowConfiguration = workFlowConfiguration;
    }
    
    private RiskInfoService riskInfoService ;
    
    public void setRiskInfoService(RiskInfoService riskInfoService) {
      this.riskInfoService = riskInfoService;
    }

    @Override
    public String startWorkFlow(String processDefinitionKey, String dataId, String paramJson, String userId) throws WorkFlowRuntimeException {
        identityService.setAuthenticatedUserId(userId);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, dataId,
                WorkFlowParseParam.parseStartWorkFlowParam(paramJson));
        LOGGER.debug("startWorkFlow paramJson: {}" , paramJson);
        return taskInstanceByProcessInstanceId(processInstance.getProcessInstanceId()).getId();
    }

    @Override
    public void complete(String taskId, String paramJson, String userId) throws WorkFlowRuntimeException {
        complete(taskId, paramJson, userId, false, false);
    }

    public void complete(String taskId, String paramJson, String userId, boolean isBack, boolean isEnded) throws WorkFlowRuntimeException {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(taskId))
            throw new NullPointerException("taskId或userId不能为空！");
        identityService.setAuthenticatedUserId(userId);
        ProcessInstance processInstance = processInstanceByTaskId(taskId); 
        taskService.addComment(taskId, processInstance.getProcessInstanceId(), WorkFlowParseParam.parseCommentParam(paramJson));
        Map<String, Object> completeMap = WorkFlowParseParam.parseCompleteParam(paramJson);
        if (isBack){
            completeMap.put(WORK_FLOW_COMPLETE_ENUM.OWNER.toString(), usersByBackTaskId(taskId));
        }
        taskService.complete(taskId, completeMap, true);
        createTodo(processInstance , isBack);
        completeTodo(taskId, isEnded , userId);
        updateBusiType(isEnded, isBack, processInstance);
    }
    
    private void updateBusiType(boolean isEnded , boolean isBack , ProcessInstance processInstance) {
      String busiType = getVariableValue(processInstance.getId(), "busiType");
      String status = "";
      if(isEnded){
        status = PROCESS_GLOBAL_STATE.UNCHECKED.toString();
      }
      if(isBack){
        status = PROCESS_GLOBAL_STATE.BACK.toString();
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        if(CollectionUtils.isNotEmpty(tasks) && tasks.size() == 1){
          Task task = tasks.get(0);
          try {
            WorkFlowConfigEntity configEntity = workFlowConfiguration.taskDefinitionEntity(processDefininiton(processInstance.getProcessDefinitionId()).getKey(), 
                                  task.getTaskDefinitionKey());
            if(PROCESS_GLOBAL_STATE.EDIT.toString().equals(configEntity.getIsEdit())){
              status = PROCESS_GLOBAL_STATE.EDIT.toString();
            }
          } catch (SQLException e) {
            throw new WorkFlowRuntimeException("查询当前环节是否可编辑出错", e);
          }
        }
      }
      Map params = new HashMap();
      params.put("busiType", busiType);
      params.put("status", status);
      params.put("pid", processInstance.getBusinessKey());
      riskInfoService.updateRiskStatus(params);
    }
    
    private void completeTodo(String taskId , boolean isEnded , String userId){
      Map<String,Object> paramsComplete = new HashMap<String, Object>();
      paramsComplete.put("userid", userId);
      paramsComplete.put("taskid", taskId);
      riskInfoService.delTask(paramsComplete);
      paramsComplete.clear();
      paramsComplete.put("completetime", new Date());
      paramsComplete.put("status", isEnded?TASK_STATE.END.toString():TASK_STATE.COMPLETE.toString());
      paramsComplete.put("taskid", taskId);
      riskInfoService.updateTask(paramsComplete);
    }
    
    private void createTodo(ProcessInstance processInstance , boolean isBack){
      if(!processInstance.isEnded()){
        List<Task> currentTasks =  tasksByProcessInstanceId(processInstance.getId());
        for(Task task : currentTasks){
          String formKey = formService.getTaskFormKey(task.getProcessDefinitionId(), task.getTaskDefinitionKey());
          Map<String,Object> params = new HashMap<String,Object>();
          params.put("url", formKey+processInstance.getBusinessKey()+"&taskId="+task.getId());
          params.put("taskid", task.getId());
          String assignee = task.getAssignee();
          if(StringUtils.isEmpty(assignee)){
            try {
              List<WorkFlowConfigEntity> workFlowConfigEntities = workFlowConfiguration.searchUnAssignees(task.getId());
              StringBuffer sb = new StringBuffer();
              for(WorkFlowConfigEntity configEntity : workFlowConfigEntities){
                sb.append(configEntity.getUserId()).append(",");
              }
              assignee = sb.substring(0,sb.length()-1);
            } catch (SQLException e) {
              throw new WorkFlowRuntimeException("查询人员出错" , e);
            }
          }
          params.put("userid", assignee);
          params.put("busitype", getVariableValue(processInstance.getId(), "busitype"));
          params.put("dataid", processInstance.getBusinessKey());
          params.put("title", getVariableValue(processInstance.getId(), "title"));
          params.put("invoiceno", getVariableValue(processInstance.getId(), "invoiceno"));
          params.put("createtime", task.getCreateTime());
          params.put("status", isBack?TASK_STATE.BACK.toString():TASK_STATE.TODO.toString());
          riskInfoService.insertTask(params);
        }
      }
    }
    
    @Override
    public void back2StartTask(String taskId, String paramJson, String userId) throws WorkFlowRuntimeException {
        String startTaskDefinitionKey ;
        try {
            startTaskDefinitionKey = workFlowConfiguration.startTaskDefinitionKeyByTaskId(
                    processInstanceByTaskId(taskId).getProcessDefinitionId()).getTaskDefKey();
        } catch (SQLException e) {
           LOGGER.error("查询起草环节的KEY出错" , e);
           throw new WorkFlowRuntimeException("查询起草环节的KEY出错" , e);
        }
        back2AnyTask(taskId, startTaskDefinitionKey, paramJson, userId);
    }

    public void back2AnyTask(String taskId, String backTaskDefinitionKey, String paramJson, String userId) throws WorkFlowRuntimeException {
        if (StringUtils.isEmpty(backTaskDefinitionKey)) {
            throw new NullPointerException("backTaskDefinitionKey不能为空！");
        }
        LOGGER.debug("back2AnyTask下backTaskDefinitionKey: {}" , backTaskDefinitionKey);
        change2TargetTaskWithoutTransition(taskId, backTaskDefinitionKey);
        complete(taskId, paramJson, userId, true , false);
    }

    @Override
    public void deployWorkFlowByZip(ZipInputStream zipInputStream) throws WorkFlowRuntimeException {
        if (zipInputStream == null){
            throw new NullPointerException("zipInputStream不能为空！");
        }
        repositoryService.createDeployment().addZipInputStream(zipInputStream).deploy();
    }

    @Override
    public void stopWorkFlow(String taskId, String paramJson, String userId) throws WorkFlowRuntimeException {
        String endDefKey ;
        try {
            endDefKey = workFlowConfiguration.endTaskDefinitionKeyByTaskId(
                        processInstanceByTaskId(taskId).getProcessDefinitionId()).getTaskDefKey();
        } catch (SQLException e) {
            throw new WorkFlowRuntimeException("查询结束环节的KEY出错" , e);
        }
        change2TargetTaskWithoutTransition(taskId, endDefKey);
        complete(taskId, paramJson, userId , false , true);
    }

    @Override
    public void claim(String taskId, String userId) throws WorkFlowRuntimeException {
        if (StringUtils.isEmpty(taskId) || StringUtils.isEmpty(userId)){
            throw new NullPointerException("taskId或userId不能为空！");
        }
        taskService.claim(taskId, userId);
    }
    
    public void admin(String taskId , String userId , String targetTaskDefKey , String paramJson) throws WorkFlowRuntimeException {
      change2TargetTaskWithoutTransition(taskId, targetTaskDefKey);
      complete(taskId, paramJson, userId);
    }


}
