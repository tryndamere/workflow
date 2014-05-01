package com.rocky.workflow;

import java.util.List;

import com.rocky.workflow.impl.json.output.WorkFlowProcessDefinitionParameter;

/**
 * 流程定义查询
 * @author rocky
 */
public interface WorkFlowProcessDefinition {
    
    List<WorkFlowProcessDefinitionParameter> getAllProcessDefinition(); 
    
}
