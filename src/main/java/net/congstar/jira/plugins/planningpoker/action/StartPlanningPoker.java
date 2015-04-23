package net.congstar.jira.plugins.planningpoker.action;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import webwork.action.ServletActionContext;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.ModifiedValue;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;


public final class StartPlanningPoker extends JiraWebActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private IssueManager issueManager;
	private JiraAuthenticationContext context=null;

	private String issueSummary;
	private Double issueStoryPoints;
	private String issueKey;

	public String getIssueKey() {
		return issueKey;
	}

	public Double getIssueStoryPoints() {
		return issueStoryPoints;
	}

	private CustomFieldManager customFieldManager;

	private PluginSettingsFactory settingsFactory;

	private String issueProjectName;

	private String issueProjectKey;

	public String getIssueProjectKey() {
		return issueProjectKey;
	}

	public String getIssueProjectName() {
		return issueProjectName;
	}

	public String getIssueSummary() {
		return issueSummary;
	}

	public StartPlanningPoker(IssueManager issueManager, CustomFieldManager customFieldManager, JiraAuthenticationContext context, PluginSettingsFactory settingsFactory) {
		this.issueManager = issueManager;
		this.customFieldManager = customFieldManager;
		this.context = context;
		this.settingsFactory = settingsFactory;		
	}
	
	@Override
	protected String doExecute() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		issueKey = request.getParameter("issueKey");
		
		
		// ApplicationUser user = context.getUser();
		
		CustomField storyPointsField = findStoryPointField();
		
		MutableIssue issue = issueManager.getIssueObject(issueKey);
		if (issue!=null) {
			issueSummary = issue.getSummary();
			issueProjectName = issue.getProjectObject().getName();
			issueProjectKey = issue.getProjectObject().getKey();
			issueStoryPoints = (Double) issue.getCustomFieldValue(storyPointsField);
			storyPointsField.updateValue(null, issue, new ModifiedValue(issueStoryPoints, new Double(8)),  new DefaultIssueChangeHolder());			
		} else {
			addErrorMessage("Issue Key"+issueKey+" not found");
			return "error";
		}
			 
		return "start";
	}

	private CustomField findStoryPointField() {
		PluginSettings settings = settingsFactory.createGlobalSettings();
		String storyPointFieldName = settings.get(ConfigurePlanningPoker.STORY_POINT_FIELD_NAME) != null ? (String) settings.get(ConfigurePlanningPoker.STORY_POINT_FIELD_NAME): "points";
		List<CustomField> field = customFieldManager.getCustomFieldObjects();
		for (Iterator<CustomField> iterator = field.iterator(); iterator.hasNext();) {
			CustomField customField = (CustomField) iterator.next();
			if (customField.getNameKey().toLowerCase().contains(storyPointFieldName)) {
				return customField;
			}
		}
		return null;
	}

}
