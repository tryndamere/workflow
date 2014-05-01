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
public class WorkFlowCommentParameter implements BaseParameter{

    /**
     * 意见选项值
     */
    private String commentOption ;

    /**
     * 意见内容
     */
    private String commentContent ;

    /**
     * 意见扩展的参数键
     */
    private List<String> commentParamKeys ;

    /**
     * 意见扩展的参数值
     */
    private List<String> commentParamValues ;

    /**
     * 意见选项
     * @return
     */
    public String getCommentOption() {
        return commentOption;
    }

    /**
     * 意见选项
     * @param commentOption
     */
    public void setCommentOption(String commentOption) {
        this.commentOption = commentOption;
    }

    /**
     * 意见内容
     * @return
     */
    public String getCommentContent() {
        return commentContent;
    }

    /**
     * 意见内容
     * @param commentContent
     */
    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    /**
     * 意见扩展的参数键
     * @return
     */
    public List<String> getCommentParamKeys() {
        return commentParamKeys;
    }

    /**
     * 意见扩展的参数键
     * @param commentParamKeys
     */
    public void setCommentParamKeys(List<String> commentParamKeys) {
        this.commentParamKeys = commentParamKeys;
    }

    /**
     * 意见扩展的参数值
     * @return
     */
    public List<String> getCommentParamValues() {
        return commentParamValues;
    }

    /**
     * 意见扩展的参数值
     * @param commentParamValues
     */
    public void setCommentParamValues(List<String> commentParamValues) {
        this.commentParamValues = commentParamValues;
    }

    private void validator(){
        if (CollectionUtils.isNotEmpty(commentParamKeys) && CollectionUtils.isNotEmpty(commentParamValues)){
            if (commentParamKeys.size() != commentParamValues.size()){
                throw new WorkFlowParseException("paramKeys和paramValues应一一对应", new IllegalAccessException("paramKeys和paramValues应一一对应"));
            }
        } else if (CollectionUtils.isEmpty(commentParamKeys) && CollectionUtils.isEmpty(commentParamValues)){
        } else {
            throw new WorkFlowParseException("paramKeys和paramValues应一一对应", new IllegalAccessException("paramKeys和paramValues应一一对应"));
        }
    }

    @Override
    public Map<String, Object> objectMap() {
        validator();
        return new HashMap<String, Object>(){
            {
                if (StringUtils.isNotEmpty(commentOption))
                    put(WORK_FLOW_COMMENT_ENUM.COMMENT_OPTION.toString(), commentOption);
                if (StringUtils.isNotEmpty(commentContent))
                    put(WORK_FLOW_COMMENT_ENUM.COMMENT_CONTENT.toString() , commentContent);
                if (CollectionUtils.isNotEmpty(commentParamKeys)){
                    for (int i = 0 , size = commentParamKeys.size() ; i < size ; i++){
                        if (StringUtils.isEmpty(commentParamKeys.get(i)) || StringUtils.isEmpty(commentParamValues.get(i)))
                            throw new WorkFlowParseException("paramKeys和paramValues均不能为空", new IllegalAccessException("paramKeys和paramValues均不能为空"));
                        put(commentParamKeys.get(i).toString(), commentParamValues.get(i));
                    }
                }
            }
        };
    }
}
