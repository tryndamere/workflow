package com.rocky.workflow.impl.json.output;

public class WorkFlowNextTask {

  private String taskName;

  private String taskDefKey;

  private String condition;

  private String taskType;

  private WorkFlowUserParameter user;

  public String getTaskType() {
    return taskType;
  }

  public void setTaskType(String taskType) {
    this.taskType = taskType;
  }

  public String getTaskName() {
    return taskName;
  }

  public void setTaskName(String taskName) {
    this.taskName = taskName;
  }

  public String getTaskDefKey() {
    return taskDefKey;
  }

  public void setTaskDefKey(String taskDefKey) {
    this.taskDefKey = taskDefKey;
  }

  public String getCondition() {
    return condition;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  public WorkFlowUserParameter getUser() {
    return user;
  }

  public void setUser(WorkFlowUserParameter user) {
    this.user = user;
  }

}
