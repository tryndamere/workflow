package com.rocky.workflow.impl.json.input;

import java.util.Map;

/**
 * Created by rocky on 14-4-7.
 */
public class WorkFlowStartParameter extends ParseBaseParameter{

    @Override
    public Map<String, Object> objectMap() {
        return baseObject2Map();
    }

}


