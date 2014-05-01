package com.rocky.workflow.impl.json.output;


public class WorkFlowParsedUser {
  private String userName ;

  private String userId ;

  private String deptId ;

  private String deptName ;

  public String getUserName() {
      return userName;
  }

  public void setUserName(String userName) {
      this.userName = userName;
  }

  public String getUserId() {
      return userId;
  }

  public void setUserId(String userId) {
      this.userId = userId;
  }

  public String getDeptId() {
      return deptId;
  }

  public void setDeptId(String deptId) {
      this.deptId = deptId;
  }

  public String getDeptName() {
      return deptName;
  }

  public void setDeptName(String deptName) {
      this.deptName = deptName;
  }
}
