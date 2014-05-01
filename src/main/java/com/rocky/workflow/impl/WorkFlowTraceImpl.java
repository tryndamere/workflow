package com.rocky.workflow.impl;

import com.alibaba.fastjson.JSON;
import com.rocky.workflow.WorkFlowTrace;
import com.rocky.workflow.exception.WorkFlowRuntimeException;
import com.rocky.workflow.impl.db.WorkFlowConfigEntity;
import com.rocky.workflow.util.CollectionUtils;
import com.rocky.workflow.util.DateStyle;
import com.rocky.workflow.util.DateUtils;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rocky on 14-4-17.
 */
public class WorkFlowTraceImpl extends WorkFlowBaseEngine implements WorkFlowTrace {
  
    private WorkFlowConfiguration workFlowConfiguration;
    
    public void setWorkFlowConfiguration(WorkFlowConfiguration workFlowConfiguration){
      this.workFlowConfiguration = workFlowConfiguration;
    }

    @Override
    public String traceText(String taskId) {
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceByTaskId(taskId).getProcessInstanceId())
                 .orderByHistoricTaskInstanceStartTime().asc().list();
        if (CollectionUtils.isNotEmpty(historicTaskInstanceList)) {
            List<Map<String,Object>> jsons = new ArrayList<Map<String,Object>>();
            for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList){
                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("taskName" , historicTaskInstance.getName());
                String assignee = historicTaskInstance.getAssignee();
                if (StringUtils.isNotEmpty(assignee)) {
                    resultMap.put("userId" , historicTaskInstance.getAssignee());
                } else {
                    List<HistoricIdentityLink> historicIdentityLinks = historyService.getHistoricIdentityLinksForTask(taskId);
                    if (CollectionUtils.isNotEmpty(historicIdentityLinks)) {
                        StringBuffer sb = new StringBuffer();
                        for (HistoricIdentityLink historicIdentityLink : historicIdentityLinks){
                            sb.append(historicIdentityLink.getUserId()).append(",");
                        }
                        resultMap.put("userId" , sb.subSequence(0, sb.length()-1));
                    }
                }
                resultMap.put("startTime" , DateUtils.DateToString(historicTaskInstance.getStartTime(), DateStyle.YYYY_MM_DD));
                resultMap.put("endTime" , DateUtils.DateToString(historicTaskInstance.getEndTime(), DateStyle.YYYY_MM_DD));
                jsons.add(resultMap);
            }
            return JSON.toJSONString(jsons);
        }
        return null;
    }

    @Override
    public InputStream traceImage(String taskId , String key) {
        ProcessDefinition processDefinition =  null ;
        if(StringUtils.isNotEmpty(taskId)){
          processDefinition = repositoryService.createProcessDefinitionQuery()
                  .processDefinitionId(processInstanceByTaskId(taskId).getProcessDefinitionId()).singleResult();
        }else{
          processDefinition = repositoryService.createProcessDefinitionQuery()
                  .processDefinitionKey(key)
                  .latestVersion().singleResult();
        }
        return repositoryService.getResourceAsStream(processDefinition.getDeploymentId() , processDefinition.getDiagramResourceName());
    }

    @Override
    public String traceText2Image(String taskId) throws WorkFlowRuntimeException {
        List<Map<String,Object>> activities = getActivityImpl(taskId);
        if(CollectionUtils.isEmpty(activities)) return "{}";
        List<Map<String , Object>> results = new ArrayList<Map<String,Object>>();
        for(Map<String,Object> activity : activities){
          ActivityImpl activityImpl = (ActivityImpl)activity.get("activityImpl");
          String currentTaskId = activity.get("taskId").toString();
          List<WorkFlowConfigEntity> currentTaskIds = new ArrayList<WorkFlowConfigEntity>();
          try {
            currentTaskIds = workFlowConfiguration.searchUnAssignees(currentTaskId);
          } catch (SQLException e) {
            throw new WorkFlowRuntimeException("查询人员出错" , e);
          }
          Map<String , Object> resultMap = new HashMap<String, Object>();
          resultMap.put("x" , activityImpl.getX());
          resultMap.put("y" , activityImpl.getY());
          resultMap.put("taskName" , activityImpl.getProperty("name"));
          resultMap.put("width" , activityImpl.getWidth());
          resultMap.put("height" , activityImpl.getHeight());
          resultMap.put("userIds" ,list2String(currentTaskIds));
          results.add(resultMap);
        }
        return JSON.toJSONString(results);
    }
    
    private String list2String(List<WorkFlowConfigEntity> currentTaskIds){
      StringBuffer sb = new StringBuffer();
      for(WorkFlowConfigEntity workFlowConfigEntity : currentTaskIds){
        sb.append(workFlowConfigEntity.getUserId()).append(",");
      }
      if(sb.length() > 0){
        return sb.substring(0 , sb.length()-1);
      }
      return sb.toString() ;
    }
}
