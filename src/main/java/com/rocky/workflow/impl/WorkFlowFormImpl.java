package com.rocky.workflow.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.rocky.workflow.WorkFlowForm;
import com.rocky.workflow.exception.WorkFlowRuntimeException;
import com.rocky.workflow.impl.json.output.WorkFlowNextTask;
import com.rocky.workflow.impl.json.output.WorkFlowParsedUser;
import com.rocky.workflow.impl.json.output.WorkFlowUserParameter;
import com.rocky.workflow.util.SpringExpressionConvert;

/**
 * Created by rocky on 14-4-15.
 */
public class WorkFlowFormImpl extends WorkFlowBaseEngine implements WorkFlowForm {

  private WorkFlowConfiguration workFlowConfiguration;

  public void setWorkFlowConfiguration(WorkFlowConfiguration workFlowConfiguration) {
    this.workFlowConfiguration = workFlowConfiguration;
  }

  @Override
  public String tools(String taskId, String processDefinitionKey) throws WorkFlowRuntimeException {
    if (StringUtils.isNotEmpty(taskId) && StringUtils.isNotEmpty(processDefinitionKey)) {
      throw new NullPointerException("taskId或processDefinitionKey必须有一个有值！");
    }
    try {
      String toolsJson;
      if (StringUtils.isNotEmpty(taskId)) {
        toolsJson = workFlowConfiguration.taskConfigByTaskId(taskId);
      } else {
        toolsJson = workFlowConfiguration.taskConfigByProcessDefinitionKey(processDefinitionKey);
      }
      return WorkFlowParseParam.parseConfig2ToolJson(toolsJson);
    } catch (SQLException e) {
      throw new WorkFlowRuntimeException("init tools", e);
    }
  }

  @Override
  public String nextTask(String taskId, String processDefinitionKey, String paramJson) throws WorkFlowRuntimeException {
    try {
      if (StringUtils.isEmpty(taskId) && StringUtils.isEmpty(processDefinitionKey)) {
        throw new NullPointerException("taskId或processDefinitionKey必须有一个有值！");
      }
      String toolsJson;
      Map<String, Object> variableMap;
      if (StringUtils.isNotEmpty(taskId)) {
        toolsJson = workFlowConfiguration.taskConfigByTaskId(taskId);
        variableMap = workFlowConfiguration.variableMap(taskId);
      } else {
        toolsJson = workFlowConfiguration.taskConfigByProcessDefinitionKey(processDefinitionKey);
        variableMap = new HashMap<String, Object>();
      }
      List<WorkFlowNextTask> workFlowNextTasks = WorkFlowParseParam.parseConfig2NextTask(toolsJson);
      variableMap.putAll(WorkFlowParseParam.parseCompleteParam(paramJson));
      
      for(WorkFlowNextTask workFlowNextTask : workFlowNextTasks){
        WorkFlowUserParameter workFlowUserParameter = workFlowNextTask.getUser();
        SpringExpressionConvert springExpressionConvert = new SpringExpressionConvert(workFlowUserParameter.getUserExp(), variableMap);
        List<WorkFlowParsedUser> workFlowParsedUsers = WorkFlowParseParam.parseUserExpJson2User(springExpressionConvert.resolverExpression().toString());
        workFlowUserParameter.setWorkFlowParsedUsers(workFlowParsedUsers);
      }
      return WorkFlowParseParam.parseWorkFlowUserParameter2ConfigJson(workFlowNextTasks);
    } catch (SQLException e) {
      throw new WorkFlowRuntimeException("nextTaskPerson", e);
    }
  }

  @Override
  public String comments(String taskId) throws WorkFlowRuntimeException {
    if (StringUtils.isEmpty(taskId))
      return null;
    return WorkFlowParseParam.parseComments2Json(workFlowConfiguration.searchAllCommentsByTaskId(taskId));
  }

}
