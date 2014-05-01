package com.rocky.workflow.exception;

/**
 * Created by rocky on 14-4-6.
 */
public class WorkFlowRuntimeException extends RuntimeException {

    public WorkFlowRuntimeException(String message , Throwable cause){
        super(message , cause);
    }

    public WorkFlowRuntimeException(String message){
        super(message);
    }

    public WorkFlowRuntimeException(Throwable cause){
        super(cause);
    }

}
