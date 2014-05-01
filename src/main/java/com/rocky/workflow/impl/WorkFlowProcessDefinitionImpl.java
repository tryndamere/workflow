package com.rocky.workflow.impl;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.repository.ProcessDefinition;

import com.rocky.workflow.WorkFlowProcessDefinition;
import com.rocky.workflow.impl.json.output.WorkFlowProcessDefinitionParameter;


public class WorkFlowProcessDefinitionImpl extends WorkFlowBaseEngine implements WorkFlowProcessDefinition{

  @Override
  public List<WorkFlowProcessDefinitionParameter> getAllProcessDefinition() {
    List<WorkFlowProcessDefinitionParameter> definitionParameters = new ArrayList<WorkFlowProcessDefinitionParameter>();
    List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionVersion().desc().list();
    for(ProcessDefinition definition : processDefinitions){
      WorkFlowProcessDefinitionParameter definitionParameter = new WorkFlowProcessDefinitionParameter();
      definitionParameter.setId(definition.getDeploymentId());
      definitionParameter.setName(definition.getName());
      definitionParameter.setKey(definition.getKey());
      String []resourceNames = definition.getResourceName().split("\\/");
      String []diagramResourceNames = definition.getDiagramResourceName().split("\\/");
      definitionParameter.setResourceName(resourceNames[resourceNames.length-1]);
      definitionParameter.setDiagramResourceName(diagramResourceNames[diagramResourceNames.length-1]);
      definitionParameters.add(definitionParameter);
    }
    return definitionParameters;
  }

}
