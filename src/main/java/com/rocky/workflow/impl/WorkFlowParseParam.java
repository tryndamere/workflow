package com.rocky.workflow.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rocky.workflow.exception.WorkFlowParseException;
import com.rocky.workflow.impl.json.input.BaseParameter;
import com.rocky.workflow.impl.json.input.WorkFlowCompleteParameter;
import com.rocky.workflow.impl.json.input.WorkFlowStartParameter;
import com.rocky.workflow.impl.json.output.WORK_FLOW_CONFIG_ENUM;
import com.rocky.workflow.impl.json.output.WorkFlowCommentOutputParameter;
import com.rocky.workflow.impl.json.output.WorkFlowNextTask;
import com.rocky.workflow.impl.json.output.WorkFlowParsedUser;
import com.rocky.workflow.impl.json.output.WorkFlowTask;

/**
 * 解析所有json格式的参数
 */
public final class WorkFlowParseParam {

    private WorkFlowParseParam() {
    }

    public static final Map<String, Object> parseStartWorkFlowParam(String paramJson) throws WorkFlowParseException {
        final BaseParameter workFlowStartParameter = JSON.parseObject(paramJson, WorkFlowStartParameter.class);
        return workFlowStartParameter.objectMap();
    }

    public static final Map<String, Object> parseCompleteParam(String paramJson) throws WorkFlowParseException {
        final BaseParameter workFlowCompleteParameter = JSON.parseObject(paramJson, WorkFlowCompleteParameter.class);
        return workFlowCompleteParameter.objectMap();
    }

    public static final String parseCommentParam(String paramJson) throws WorkFlowParseException {
        final BaseParameter workFlowCommentParameter = JSON.parseObject(paramJson, WorkFlowCompleteParameter.class)
                .getWorkFlowCommentParameter();
        return JSON.toJSON(workFlowCommentParameter.objectMap()).toString();
    }

    public static final String parseConfig2ToolJson(String paramJson) throws WorkFlowParseException {
        JSONObject jsonObject = JSON.parseObject(paramJson);
        return jsonObject.getJSONObject(WORK_FLOW_CONFIG_ENUM.TOOLS.toString()).toJSONString();
    }

    public static final List<WorkFlowNextTask> parseConfig2NextTask(String paramJson) throws WorkFlowParseException {
        JSONObject jsonObject = JSON.parseObject(paramJson);
        return JSON.parseArray(jsonObject.getJSONArray(WORK_FLOW_CONFIG_ENUM.NEXT_TASKS.toString()).toJSONString(),
                WorkFlowNextTask.class);
    }

    public static final List<WorkFlowParsedUser> parseUserExpJson2User(String userExpJson) throws WorkFlowParseException {
        return JSON.parseArray(userExpJson, WorkFlowParsedUser.class);
    }

    public static final String parseWorkFlowUserParameter2ConfigJson(List<WorkFlowNextTask> workFlowNextTasks) throws WorkFlowParseException {
        return JSON.toJSONString(workFlowNextTasks);
    }

    public static final Map<String, String> parseWorkFlowFullMsg2CommentMap(String fullMsg) throws WorkFlowParseException {
        return JSON.parseObject(fullMsg, HashMap.class);
    }

    public static final String parseComments2Json(List<WorkFlowCommentOutputParameter> workFlowCommentOutputParameters) throws WorkFlowParseException {
        return JSON.toJSONString(workFlowCommentOutputParameters);
    }

    public static final String parseTasks2Json(List<WorkFlowTask> tasks) {
        return JSON.toJSONString(tasks);
    }

    public static void main(String[] args) {
    }


}
