package com.rocky.workflow.impl;

import java.util.Map;


public interface RiskInfoService {
  
  public abstract String insertTask(Map params);
  
  /**
   * 更新代办表数据
   */
  public abstract int updateTask(Map params);
  
  /**
   * 删除代办表数据
   */
  public abstract int delTask(Map params);
  
  public abstract int updateRiskStatus(Map params);
  
}
