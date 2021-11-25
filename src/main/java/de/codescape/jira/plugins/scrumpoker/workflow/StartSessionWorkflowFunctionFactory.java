package de.codescape.jira.plugins.scrumpoker.workflow;

import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginFunctionFactory;
import com.opensymphony.workflow.loader.AbstractDescriptor;

import java.util.Map;

/**
 * Factory class for the {@link StartSessionWorkflowFunction}. This factory class is mainly empty because the function
 * does not require any configuration options yet.
 */
public class StartSessionWorkflowFunctionFactory extends AbstractWorkflowPluginFactory implements WorkflowPluginFunctionFactory {

    @Override
    public Map<String, ?> getDescriptorParams(Map<String, Object> map) {
        return map;
    }

    @Override
    protected void getVelocityParamsForInput(Map<String, Object> map) {
        // intentionally left blank
    }

    @Override
    protected void getVelocityParamsForEdit(Map<String, Object> map, AbstractDescriptor abstractDescriptor) {
        // intentionally left blank
    }

    @Override
    protected void getVelocityParamsForView(Map<String, Object> map, AbstractDescriptor abstractDescriptor) {
        // intentionally left blank
    }

}
