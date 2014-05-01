package com.rocky.workflow.impl.json.output;

/**
 * Created by rocky on 14-4-15.
 */
public enum WORK_FLOW_CONFIG_ENUM {
    TOOLS("tools") , NEXT_TASKS("nextTasks") , BACK_TASKS("backTasks") , USERS("user") , USER_EXP("userExp");

    private String value;

    WORK_FLOW_CONFIG_ENUM(String value){
        this.value = value;
    }

    public String toString(){
        return this.value;
    }
}
