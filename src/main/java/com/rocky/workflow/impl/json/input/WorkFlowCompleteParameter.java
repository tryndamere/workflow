package com.rocky.workflow.impl.json.input;

import java.util.Map;

/**
 * Created by rocky on 14-4-7.
 */
public class WorkFlowCompleteParameter extends ParseBaseParameter {

  /**
   * 下个环节处理人
   */
  private String owner;

  /**
   * 条件
   */
  private String condition;

  public String getCondition() {
    return condition;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  /**
   * 下个环节处理人
   * 
   * @param owner
   */
  public void setOwner(String owner) {
    this.owner = owner;
  }

  /**
   * 下个环节处理人
   * 
   * @return
   */
  public String getOwner() {
    return owner;
  }

  /**
   * 意见对象
   */
  private WorkFlowCommentParameter workFlowCommentParameter;

  /**
   * 意见对象
   * 
   * @return
   */
  public WorkFlowCommentParameter getWorkFlowCommentParameter() {
    return workFlowCommentParameter;
  }

  /**
   * 意见对象
   * 
   * @param workFlowCommentParameter
   */
  public void setWorkFlowCommentParameter(WorkFlowCommentParameter workFlowCommentParameter) {
    this.workFlowCommentParameter = workFlowCommentParameter;
  }

  @Override
  public Map<String, Object> objectMap() {
    Map<String, Object> resultMap = baseObject2Map();
    resultMap.put(WORK_FLOW_COMPLETE_ENUM.OWNER.toString(), owner);
    resultMap.put(WORK_FLOW_COMPLETE_ENUM.CONDITION.toString(), condition);
    return resultMap;
  }
}
