package com.rocky.workflow.impl.json.output;

import java.util.List;

/**
 * Created by rocky on 14-4-15.
 */
public class WorkFlowUserParameter {

  private String isSelectUser;

  private String isMultiple;

  private String userExp;

  private List<WorkFlowParsedUser> workFlowParsedUsers;

  public List<WorkFlowParsedUser> getWorkFlowParsedUsers() {
    return workFlowParsedUsers;
  }

  public void setWorkFlowParsedUsers(List<WorkFlowParsedUser> workFlowParsedUsers) {
    this.workFlowParsedUsers = workFlowParsedUsers;
  }

  public String getIsSelectUser() {
    return isSelectUser;
  }

  public void setIsSelectUser(String isSelectUser) {
    this.isSelectUser = isSelectUser;
  }

  public String getIsMultiple() {
    return isMultiple;
  }

  public void setIsMultiple(String isMultiple) {
    this.isMultiple = isMultiple;
  }

  public String getUserExp() {
    return userExp;
  }

  public void setUserExp(String userExp) {
    this.userExp = userExp;
  }

}
