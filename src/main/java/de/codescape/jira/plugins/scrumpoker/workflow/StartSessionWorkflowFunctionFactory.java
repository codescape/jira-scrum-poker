package de.codescape.jira.plugins.scrumpoker.workflow;

import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginFunctionFactory;
import com.opensymphony.workflow.loader.AbstractDescriptor;

import java.util.Map;

/**
 * Factory class for the {@link StartSessionWorkflowFunction}.
 */
public class StartSessionWorkflowFunctionFactory extends AbstractWorkflowPluginFactory
    implements WorkflowPluginFunctionFactory {

    @Override
    public Map<String, ?> getDescriptorParams(Map<String, Object> map) {
        return map;
    }

    @Override
    protected void getVelocityParamsForInput(Map<String, Object> map) {
    }

    @Override
    protected void getVelocityParamsForEdit(Map<String, Object> map, AbstractDescriptor abstractDescriptor) {
    }

    @Override
    protected void getVelocityParamsForView(Map<String, Object> map, AbstractDescriptor abstractDescriptor) {
    }

}
