package com.rocky.workflow.impl.json.input;

import com.rocky.workflow.exception.WorkFlowParseException;
import com.rocky.workflow.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rocky on 14-4-7.
 */
public abstract class ParseBaseParameter implements BaseParameter{

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 参数键
     */
    private List<String> paramKey;

    /**
     * 参数值
     */
    private List<Object> paramValue;

    /**
     * 获取部门ID
     *
     * @return
     */
    public String getDeptId() {
        return deptId;
    }

    /**
     * 设置部门ID
     *
     * @param deptId 部门ID
     */
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    /**
     * 获取参数键
     *
     * @return
     */
    public List<String> getParamKey() {
        return paramKey;
    }

    /**
     * 设置参数键
     *
     * @param paramKeys 参数键
     */
    public void setParamKey(List<String> paramKey) {
        this.paramKey = paramKey;
    }

    /**
     * 获取参数 获取参数值
     *
     * @return
     */
    public List<Object> getParamValue() {
        return paramValue;
    }

    /**
     * 设置参数值
     *
     * @param paramValues 参数值
     */
    public void setParamValue(List<Object> paramValue) {
        this.paramValue = paramValue;
    }

    /**
     * 将domain对象转化成map
     * @return
     */
    public abstract Map<String, Object> objectMap();

    /**
     * 当前对象转化成MAP
     *
     * @return
     */
    protected Map<String, Object> baseObject2Map() {
        validatorParam();
        return new HashMap<String, Object>() {
            {
                put(WORK_FLOW_BASE_PARAM_ENUM.DEPT_ID.toString(), getDeptId());
                if(CollectionUtils.isNotEmpty(paramKey)){
                  for (int i = 0, size = paramKey.size(); i < size; i++) {
                      if (paramKey.get(i) == null || paramValue.get(i) == null){
                          throw new WorkFlowParseException("paramKeys或paramValues不能为空", new NullPointerException("paramKeys或paramValues不能为空"));
                      }
                      put(paramKey.get(i).toString(), paramValue.get(i));
                  }
                }
            }
        };
    }

    /**
     * 验证参数的有效性
     */
    private void validatorParam() {
        if (StringUtils.isEmpty(getDeptId())) {
            throw new WorkFlowParseException("deptId为空", new NullPointerException("deptId为空"));
        }
        if (CollectionUtils.isNotEmpty(paramKey) && CollectionUtils.isNotEmpty(paramValue)){
            if (paramKey.size() != paramValue.size()){
                throw new WorkFlowParseException("paramKeys和paramValues应一一对应", new IllegalAccessException("paramKeys和paramValues应一一对应"));
            }
        } else if (CollectionUtils.isEmpty(paramKey) && CollectionUtils.isEmpty(paramValue)){
        } else {
            throw new WorkFlowParseException("paramKeys和paramValues应一一对应", new IllegalAccessException("paramKeys和paramValues应一一对应"));
        }

    }
}
