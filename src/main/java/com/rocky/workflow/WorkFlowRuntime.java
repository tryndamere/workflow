package com.rocky.workflow;

import com.rocky.workflow.exception.WorkFlowRuntimeException;

import java.sql.SQLException;
import java.util.zip.ZipInputStream;

/**
 * 流程运行时功能点
 * Created by rocky on 14-4-6.
 *
 */
public interface WorkFlowRuntime {
  
    public enum TASK_STATE{
      TODO("0") , COMPLETE("1") , BACK("2") , END("3");
      
      private String value ;
      
      private TASK_STATE(String value){
        this.value = value ;
      }
      
      public String toString(){
        return value;
      }
    }
    
    public enum PROCESS_GLOBAL_STATE{
      CHECK("0") , UNCHECKED("1") , EDIT("2") , BACK("3");
      
      private String value ;
      
      private PROCESS_GLOBAL_STATE(String value){
        this.value = value;
      }
      
      public String toString(){
        return value ;
      }
    }

    /**
     * 启动流程
     * @param processDefinitionKey  流程定义的KEY
     * @param dataId      业务主键
     * @param paramJson   参数值  非空
     * @param userId 起草人
     * @return 当前环节实例ID
     * @throws WorkFlowRuntimeException
     */
    public String startWorkFlow(String processDefinitionKey, String dataId, String paramJson, String userId) throws WorkFlowRuntimeException;

    /**
     * 完成当前任务
     * @param taskId      当前环节实例ID
     * @param paramJson   流程完成时参数 非空
     * @param userId      当前处理的用户
     * @throws WorkFlowRuntimeException
     */
    public void complete(String taskId, String paramJson, String userId) throws WorkFlowRuntimeException;

    /**
     * 退回到起草环节
     * @param taskId      当前环节实例ID
     * @param paramJson   流程完成时参数 非空
     * @param userId      当前处理的用户
     * @throws WorkFlowRuntimeException
     */
    public void back2StartTask(String taskId, String paramJson, String userId) throws WorkFlowRuntimeException, SQLException;

    /**
     * 部署流程
     * @param zipInputStream 部署的zip包流文件，zip包含2个文件：一个bpmn， 一个image图片
     * @throws WorkFlowRuntimeException
     */
    public void deployWorkFlowByZip(ZipInputStream zipInputStream) throws WorkFlowRuntimeException;

    /**
     * 终止流程
     * @param taskId 当前环节实例ID
     * @param paramJson   流程完成时参数 可为null
     * @param userId      当前处理的用户
     * @throws WorkFlowRuntimeException
     */
    public void stopWorkFlow(String taskId, String paramJson, String userId) throws WorkFlowRuntimeException;

    /**
     * 签收
     * @param taskId 当前环节实例ID
     * @param userId 签收人
     * @throws WorkFlowRuntimeException
     */
    public void claim(String taskId , String userId) throws WorkFlowRuntimeException;
    
    /**
     * 管理员任意流转
     * @param taskId 当前环节实例ID
     * @param userId 签收人
     * @param targetTaskDefKey 目标节点定义名称
     * @param paramJson 流程中参数
     * @throws WorkFlowRuntimeException
     */
    public void admin(String taskId , String userId , String targetTaskDefKey , String paramJson) throws WorkFlowRuntimeException;

}
