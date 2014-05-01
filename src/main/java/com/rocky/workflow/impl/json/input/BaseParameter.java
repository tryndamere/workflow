package com.rocky.workflow.impl.json.input;

import java.util.Map;

/**
 * Created by rocky on 14-4-7.
 */
public interface BaseParameter {

    /**
     * 将domain对象转化成map
     * @return
     */
    public abstract Map<String, Object> objectMap();

}
