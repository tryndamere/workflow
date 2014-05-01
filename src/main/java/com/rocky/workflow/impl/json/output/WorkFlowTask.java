package com.rocky.workflow.impl.json.output;

/**
 * Created by rocky on 14-4-17.
 */
public class WorkFlowTask {

    /**
     * 环节ID
     */
    private String taskId ;

    /**
     * 用户ID
     */
    private String userId ;

    /**
     * 业务主键ID
     */
    private String dataId ;

    /**
     * 当前环节名称
     */
    private String currentTaskName ;

    /**
     * 创建时间
     */
    private String createTime ;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCurrentTaskName() {
        return currentTaskName;
    }

    public void setCurrentTaskName(String currentTaskName) {
        this.currentTaskName = currentTaskName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }
}
