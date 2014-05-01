package com.rocky.workflow.impl.json.input;

/**
 * Created by rocky on 14-4-7.
 */
public enum WORK_FLOW_BASE_PARAM_ENUM {

    DEPT_ID("deptId") , PARAM_KEY("paramKey") , PARAM_VALUE("paramValue");

    private String resultValue ;

    WORK_FLOW_BASE_PARAM_ENUM(String resultValue){
       this.resultValue = resultValue;
    }

    public String toString(){
        return resultValue;
    }
}
