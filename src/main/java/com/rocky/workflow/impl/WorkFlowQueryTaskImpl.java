package com.rocky.workflow.impl;

import com.rocky.workflow.WorkFlowQueryTask;
import com.rocky.workflow.impl.json.output.WorkFlowTask;
import com.rocky.workflow.util.CollectionUtils;
import com.rocky.workflow.util.DateStyle;
import com.rocky.workflow.util.DateUtils;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rocky on 14-4-16.
 */
public class WorkFlowQueryTaskImpl extends WorkFlowBaseEngine implements WorkFlowQueryTask {

    @Override
    public String queryTodoTasksByUserId(String userId) {
       return queryPageTodoTasksByUserId(userId, 0, Integer.MAX_VALUE);
    }

    @Override
    public String queryCompleteTasksByUserId(String userId) {
       return queryPageCompleteTasksByUserId(userId, 0, Integer.MAX_VALUE);
    }

    @Override
    public String queryPageTodoTasksByUserId(String userId, int start, int end) {
        List<Task> tasks = taskService.createTaskQuery().taskCandidateUser(userId).listPage(start, end);
        tasks.addAll(taskService.createTaskQuery().taskAssignee(userId).listPage(start, end));
        if (CollectionUtils.isNotEmpty(tasks)) {
            List<WorkFlowTask> workFlowTasks = new ArrayList<WorkFlowTask>();
            for (Task task : tasks) {
                WorkFlowTask workFlowTask = new WorkFlowTask();
                workFlowTask.setUserId(userId);
                workFlowTask.setTaskId(task.getId());
                workFlowTask.setCurrentTaskName(task.getName());
                workFlowTask.setDataId(processInstanceByTaskId(task.getId()).getBusinessKey());
                workFlowTask.setCreateTime(DateUtils.DateToString(task.getCreateTime(), DateStyle.YYYY_MM_DD));
                workFlowTasks.add(workFlowTask);
            }
            return WorkFlowParseParam.parseTasks2Json(workFlowTasks);
        }
        return null;
    }

    @Override
    public String queryPageCompleteTasksByUserId(String userId, int start, int end) {
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().taskAssignee(userId).finished().listPage(start,end);
        if (CollectionUtils.isNotEmpty(historicTaskInstances)) {
            List<WorkFlowTask> workFlowTasks = new ArrayList<WorkFlowTask>();
            for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
                WorkFlowTask workFlowTask = new WorkFlowTask();
                workFlowTask.setUserId(userId);
                workFlowTask.setTaskId(historicTaskInstance.getId());
                workFlowTask.setCreateTime(DateUtils.DateToString(historicTaskInstance.getStartTime(), DateStyle.YYYY_MM_DD));
                workFlowTask.setDataId(processInstanceByTaskId(historicTaskInstance.getId()).getBusinessKey());
                workFlowTask.setCurrentTaskName(historicTaskInstance.getName());
                workFlowTasks.add(workFlowTask);
            }
            return WorkFlowParseParam.parseTasks2Json(workFlowTasks);
        }
        return null;
    }

    @Override
    public String isComplete(String taskId) {
      return historyService.createHistoricTaskInstanceQuery().taskId(taskId).finished().singleResult() == null ? "0" : "1";
    }
}
