package com.rocky.workflow.impl.json.input;

/**
 * Created by rocky on 14-4-7.
 */
public enum WORK_FLOW_COMMENT_ENUM {

    COMMENT_OPTION("commentOption"), COMMENT_CONTENT("commentContent");

    private String resultValue ;

    WORK_FLOW_COMMENT_ENUM(String resultValue){
        this.resultValue = resultValue;
    }

    public String toString(){
        return resultValue;
    }


}
