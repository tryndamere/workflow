package com.rocky.workflow.impl;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.rocky.workflow.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 流程操作核心类<br>
 * 此核心类主要处理：流程通过、驳回、转办、中止、挂起等核心操作<br>
 */
public abstract class WorkFlowBaseEngine {
    
    protected RepositoryService repositoryService;

    protected RuntimeService runtimeService;

    protected TaskService taskService;

    protected HistoryService historyService;

    protected IdentityService identityService;

    protected ManagementService managementService;
    
    protected FormService formService;
    
    public void setFormService(FormService formService) {
      this.formService = formService;
    }

    public void setRepositoryService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    public void setManagementService(ManagementService managementService) {
        this.managementService = managementService;
    }

    /**
     * 通过流程实例获取环节实例
     *
     * @param processInstanceId 流程实例ID
     * @return 当前环节实例
     */
    protected Task taskInstanceByProcessInstanceId(String processInstanceId) {
        return taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
    }
    
    protected List<Task> tasksByProcessInstanceId(String processInstanceId){
        return taskService.createTaskQuery().processInstanceId(processInstanceId).list();
    }

    /**
     * 通过任务实例ID获取流程实例对象
     *
     * @param taskId
     * @return
     */
    protected ProcessInstance processInstanceByTaskId(String taskId) {
        return runtimeService.createProcessInstanceQuery()
                .processInstanceId(historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult().getProcessInstanceId()).singleResult();
    }

    protected HistoricTaskInstance historicTaskInstanceByTaskId(String taskId) {
        return historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
    }
    
    protected String getVariableValue(String processInstanceId , String variableName){
      return (String)historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).variableName(variableName).singleResult().getValue();
    }

    /**
     * 任意流
     *
     * @param taskId           当前环节实例ID
     * @param targetTaskDefKey 目标环节定义的KEY
     */
    protected void change2TargetTaskWithoutTransition(String taskId, String targetTaskDefKey) {
        Task currentTask = taskInstanceByProcessInstanceId(taskId);
        ProcessDefinitionImpl processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)    
                .getDeployedProcessDefinition(historicTaskInstanceByTaskId(taskId).getProcessDefinitionId());
        ActivityImpl activity = processDefinition.findActivity(currentTask.getTaskDefinitionKey());
        activity.getOutgoingTransitions().clear();
        activity.createOutgoingTransition().setDestination(processDefinition.findActivity(targetTaskDefKey));
    }

    private ProcessDefinitionEntity getProcessDefinitionImpl(String taskId , String processDefinitionKey) {
        if (StringUtils.isEmpty(taskId)){
          return  null;
        } else {
          return (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)    
                  .getDeployedProcessDefinition(historicTaskInstanceByTaskId(taskId).getProcessDefinitionId());
        }
    }
    
    protected ProcessDefinition processDefininiton(String processDefinitionId){
      return repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
    }
    
    

    /**
     * 获取当前的定义内容
     * @param taskId
     * @return
     */
    protected List<Map<String,Object>> getActivityImpl(String taskId) {
        List<Map<String,Object>> results = new ArrayList<Map<String,Object>>();
        HistoricTaskInstance currentTask = historicTaskInstanceByTaskId(taskId);
        ProcessDefinitionEntity processDefinition = getProcessDefinitionImpl(taskId, currentTask.getProcessDefinitionId());
        if(processDefinition != null){
          List<Task> tasks = tasksByProcessInstanceId(currentTask.getProcessInstanceId());
          if(CollectionUtils.isNotEmpty(tasks)){
            String latestTaskDefKey = "";
            for(Task task : tasks){
              if(!latestTaskDefKey.equals(task.getTaskDefinitionKey())){
                Map<String,Object> resultMap = new HashMap<String, Object>();
                resultMap.put("activityImpl" , processDefinition.findActivity(task.getTaskDefinitionKey()));
                resultMap.put("taskId" , task.getId());
                results.add(resultMap);
              }
              latestTaskDefKey = task.getTaskDefinitionKey();
            }
          }else{
              HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().processInstanceId(currentTask.getProcessInstanceId()).
              orderByHistoricTaskInstanceStartTime().desc().list().get(0);
              Map<String,Object> resultMap = new HashMap<String, Object>();
              resultMap.put("activityImpl" , processDefinition.findActivity(task.getTaskDefinitionKey()));
              resultMap.put("taskId" , task.getId());
              results.add(resultMap);
          }
        }
        return results;
    }
    /**
     * 获取退回节点的人员
     * @param taskId 当前环节实例ID
     * @return
     */
    protected String usersByBackTaskId(String taskId) {
        HistoricTaskInstance historicTaskInstance = historicTaskInstanceByTaskId(taskId);
        return historicTaskInstance.getAssignee();
    }

    /**
     * 获取当前流程中的参数
     * @param taskId 当前环节实例ID
     * @return
     */
    protected Map<String, Object> variableMap(String taskId) {
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().includeProcessVariables()
                    .includeTaskLocalVariables().taskId(taskId).singleResult();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.putAll(historicTaskInstance.getProcessVariables());
        resultMap.putAll(historicTaskInstance.getTaskLocalVariables());
        return resultMap;
    }

    /**
     * 获取流程中所有意见
     * @param taskId 当前环节实例ID
     * @return
     */
    protected List<Comment> allCommentsByTaskId(String taskId){
        return taskService.getProcessInstanceComments(processInstanceByTaskId(taskId).getProcessInstanceId());
    }


}
