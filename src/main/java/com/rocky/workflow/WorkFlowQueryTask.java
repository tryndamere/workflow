package com.rocky.workflow;

/**
 * Created by rocky on 14-4-6.
 */
public interface WorkFlowQueryTask {
    
    /**
     * 判断当前环节是否已为已办
     * @param taskId 待办ID
     */
    public String isComplete(String taskId);

    /**
     * 查询当前用户所有待办信息
     * @param userId 当前用户ID
     * @return json格式串
     */
    public String queryTodoTasksByUserId(String userId);

    /**
     * 查询当前用户所有已办信息
     * @param userId 当前用户ID
     * @return json格式串
     */
    public String queryCompleteTasksByUserId(String userId);

    /**
     * 查询当前用户待办，按照分页来显示
     * @param userId 用户ID
     * @param start  游标起点
     * @param end    游标终点
     * @return json格式串
     */
    public String queryPageTodoTasksByUserId(String userId , int start , int end);

    /**
     * 查询当前用户已办，按照分页来显示
     * @param userId 用户ID
     * @param start  游标起点
     * @param end    游标终点
     * @return
     */
    public String queryPageCompleteTasksByUserId(String userId , int start , int end);
}
