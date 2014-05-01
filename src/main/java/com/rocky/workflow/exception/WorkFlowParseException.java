package com.rocky.workflow.exception;

/**
 * Created by rocky on 14-4-7.
 */
public class WorkFlowParseException extends RuntimeException {

    public WorkFlowParseException(String message, Throwable cause){
        super(message, cause);
    }

    public WorkFlowParseException(String message){
        super(message);
    }

    public WorkFlowParseException(Throwable cause){
        super(cause);
    }
}
