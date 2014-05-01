package com.rocky.workflow.util;


import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.spring.SpringExpressionManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringExpressionManagerFactoryBean implements FactoryBean, ApplicationContextAware {
  
  ApplicationContext applicationContext;
  ProcessEngineConfiguration processEngineConfiguration;
  
  
  @Override
  public Object getObject() throws Exception {
    return new SpringExpressionManager(applicationContext, ((ProcessEngineConfigurationImpl) processEngineConfiguration).getBeans());
  }

  @Override
  public Class getObjectType() {
    return SpringExpressionManager.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext)
      throws BeansException {
    this.applicationContext = applicationContext;
  }

  public void setProcessEngineConfiguration(
      ProcessEngineConfiguration processEngineConfiguration) {
    this.processEngineConfiguration = processEngineConfiguration;
  }
  
}
