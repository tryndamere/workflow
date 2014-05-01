package com.rocky.workflow.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Comment;

import com.rocky.workflow.impl.db.DbUtilsDao;
import com.rocky.workflow.impl.db.WorkFlowConfigEntity;
import com.rocky.workflow.impl.json.output.WorkFlowCommentOutputParameter;
import com.rocky.workflow.util.CollectionUtils;
import com.rocky.workflow.util.DateStyle;
import com.rocky.workflow.util.DateUtils;

/**
 * Created by rocky on 14-4-7.
 */
public class WorkFlowConfiguration extends WorkFlowBaseEngine{

    private DbUtilsDao<WorkFlowConfigEntity> dbUtilsDao;

    public void setDbUtilsDao(DbUtilsDao<WorkFlowConfigEntity> dbUtilsDao) {
        this.dbUtilsDao = dbUtilsDao;
    }

    private static final String SEARCH_TASK_TYPE_SQL = "SELECT CONFIG_ID AS configId , PROCESS_DEF_KEY AS processDefKey , TASK_DEF_KEY AS taskDefKey , TASK_TYPE AS taskType , TASK_CONFIG AS taskConfig , IS_TASK_EDIT AS isEdit FROM WF_TASK_CONFIG WHERE PROCESS_DEF_KEY = ? AND TASK_TYPE = ?" ;

    private static final String SEARCH_TASK_CONFIG_SQL = "SELECT CONFIG_ID AS configId , PROCESS_DEF_KEY AS processDefKey , TASK_DEF_KEY AS taskDefKey , TASK_TYPE AS taskType , TASK_CONFIG AS taskConfig, IS_TASK_EDIT AS isEdit FROM WF_TASK_CONFIG WHERE PROCESS_DEF_KEY = ? AND TASK_DEF_KEY = ?";
    
    private static final String SEARCH_TASK_ASSIGNEE = "SELECT USER_ID_ AS userId FROM ACT_RU_IDENTITYLINK WHERE TASK_ID_= ?";
    
    public WorkFlowConfigEntity taskDefinitionEntity(String processDefinitionKey , String taskDefinitionKey) throws SQLException {
      return dbUtilsDao.selectOne(SEARCH_TASK_CONFIG_SQL, WorkFlowConfigEntity.class, new Object[]{processDefinitionKey , taskDefinitionKey});
    }
    
    /**
     * 通过环节实例ID获取流程配置实例集合
     * @param taskId 任务ID
     * @return 
     */
    public List<WorkFlowConfigEntity> searchUnAssignees(String taskId) throws SQLException {
      return dbUtilsDao.select(SEARCH_TASK_ASSIGNEE, WorkFlowConfigEntity.class, new Object[]{taskId});
    }
    
    /**
     * 通过流程定义KEY获取起草环节KEY
     * @param taskId 流程环节实例ID
     * @return
     */
    public WorkFlowConfigEntity startTaskDefinitionKeyByTaskId(String taskId) throws SQLException {
        String processDefinitionKey = super.processInstanceByTaskId(taskId).getProcessDefinitionId();
        return dbUtilsDao.selectOne(SEARCH_TASK_TYPE_SQL, WorkFlowConfigEntity.class, new Object[]{processDefinitionKey, "0"});
    }
    
    /**
     * 通过流程定义的KEY获取起草环节配置
     * @param processDefinitionKey 流程定义KEY 
     * @return
     */
    public String taskConfigByProcessDefinitionKey(String processDefinitionKey) throws SQLException {
      return dbUtilsDao.selectOne(SEARCH_TASK_TYPE_SQL, WorkFlowConfigEntity.class, new Object[]{processDefinitionKey, "0"}).getTaskConfig();
    }

    /**
     * 获取工具条内容
     * @param taskId 流程环节实例ID
     * @return
     */
    public String taskConfigByTaskId(String taskId) throws SQLException {
        HistoricTaskInstance historicTaskInstance = super.historicTaskInstanceByTaskId(taskId) ;
        String taskDefKey = historicTaskInstance.getTaskDefinitionKey() ;
        String processDefKey = repositoryService.createProcessDefinitionQuery().processDefinitionId(historicTaskInstance.getProcessDefinitionId()).singleResult().getKey();
        WorkFlowConfigEntity workFlowConfigEntity = dbUtilsDao.selectOne(SEARCH_TASK_CONFIG_SQL, WorkFlowConfigEntity.class , new Object[]{processDefKey, taskDefKey});
        return workFlowConfigEntity.getTaskConfig();
    }

    /**
     * 通过流程定义节点
     * @param taskId 流程环节实例ID
     * @return 
     * @throws SQLException
     */
    public WorkFlowConfigEntity endTaskDefinitionKeyByTaskId(String taskId) throws SQLException {
        String processDefinitionKey = super.processInstanceByTaskId(taskId).getProcessDefinitionId();
        return dbUtilsDao.selectOne(SEARCH_TASK_TYPE_SQL, WorkFlowConfigEntity.class, new Object[]{processDefinitionKey, "2"});
    }

    /**
     * 查询出意见内容
     * @param taskId 流程环节实例ID
     * @return
     */
    public List<WorkFlowCommentOutputParameter> searchAllCommentsByTaskId(String taskId) {
        List<Comment> comments = allCommentsByTaskId(taskId);
        if (CollectionUtils.isNotEmpty(comments)){
            List<WorkFlowCommentOutputParameter> workFlowCommentOutputParameters = new ArrayList<WorkFlowCommentOutputParameter>();
            for (Comment comment : comments) {
                WorkFlowCommentOutputParameter workFlowCommentOutputParameter = new WorkFlowCommentOutputParameter();
                workFlowCommentOutputParameter.setUserId(comment.getUserId());
                workFlowCommentOutputParameter.setTaskDefKey(historicTaskInstanceByTaskId(comment.getTaskId()).getTaskDefinitionKey());
                workFlowCommentOutputParameter.setFullMsg(WorkFlowParseParam.parseWorkFlowFullMsg2CommentMap(comment.getFullMessage()));
                workFlowCommentOutputParameter.setTime(DateUtils.getTime(comment.getTime() , DateStyle.YYYY_MM_DD));
                workFlowCommentOutputParameters.add(workFlowCommentOutputParameter);
            }
            return workFlowCommentOutputParameters;
        }
        return null;
    }
}
