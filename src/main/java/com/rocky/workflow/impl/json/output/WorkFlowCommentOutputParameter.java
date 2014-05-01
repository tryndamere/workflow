package com.rocky.workflow.impl.json.output;

import java.util.Map;

/**
 * Created by rocky on 14-4-16.
 */
public class WorkFlowCommentOutputParameter {

    private String taskDefKey ;

    private Map<String, String> fullMsg ;

    private String userId ;

    private String time ;

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public Map<String, String> getFullMsg() {
        return fullMsg;
    }

    public void setFullMsg(Map<String, String> fullMsg) {
        this.fullMsg = fullMsg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
