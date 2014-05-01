package com.rocky.workflow.impl.db;

/**
 * Created by rocky on 14-4-9.
 */
public class WorkFlowConfigEntity {

  /**
   * 主键
   */
  private String configId;

  /**
   * 流程定义的KEY
   */
  private String processDefKey;

  /**
   * 环节定义的KEY
   */
  private String taskDefKey;

  /**
   * 环节类型
   */
  private String taskType;

  /**
   * 环节配置
   */
  private String taskConfig;

  /**
   * 人员ID
   */
  private String userId;

  /**
   * 是否可编辑
   */
  private String isEdit;

  public String getIsEdit() {
    return isEdit;
  }

  public void setIsEdit(String isEdit) {
    this.isEdit = isEdit;
  }

  /**
   * 人员获取
   */
  public String getUserId() {
    return userId;
  }

  /**
   * 人员获取
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }

  /**
   * 主键
   * 
   * @return
   */
  public String getConfigId() {
    return configId;
  }

  /**
   * 主键
   * 
   * @param configId
   */
  public void setConfigId(String configId) {
    this.configId = configId;
  }

  /**
   * 流程定义KEY
   * 
   * @return
   */
  public String getProcessDefKey() {
    return processDefKey;
  }

  /**
   * 流程定义KEY
   * 
   * @param processDefKey
   */
  public void setProcessDefKey(String processDefKey) {
    this.processDefKey = processDefKey;
  }

  /**
   * 环节定义KEY
   * 
   * @return
   */
  public String getTaskDefKey() {
    return taskDefKey;
  }

  /**
   * 环节定义KEY
   * 
   * @param taskDefKey
   */
  public void setTaskDefKey(String taskDefKey) {
    this.taskDefKey = taskDefKey;
  }

  /**
   * 环节类型 0 起草
   * 
   * @return
   */
  public String getTaskType() {
    return taskType;
  }

  /**
   * 环节类型
   * 
   * @param taskType
   */
  public void setTaskType(String taskType) {
    this.taskType = taskType;
  }

  /**
   * 环节配置
   * 
   * @return
   */
  public String getTaskConfig() {
    return taskConfig;
  }

  /**
   * 环节配置
   * 
   * @param taskConfig
   */
  public void setTaskConfig(String taskConfig) {
    this.taskConfig = taskConfig;
  }
}
