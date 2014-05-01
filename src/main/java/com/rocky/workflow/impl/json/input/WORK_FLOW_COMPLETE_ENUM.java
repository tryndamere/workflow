package com.rocky.workflow.impl.json.input;

/**
 * Created by rocky on 14-4-14.
 */
public enum WORK_FLOW_COMPLETE_ENUM {
    OWNER("owner") , CONDITION("condition");

    private String resultValue ;

    WORK_FLOW_COMPLETE_ENUM(String resultValue){
        this.resultValue = resultValue;
    }

    public String toString(){
        return resultValue;
    }

}
