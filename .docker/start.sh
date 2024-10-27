#!/bin/bash

mkdir -p /var/atlassian/application-data/jira/plugins/installed-plugins

cp /opt/dbconfig.xml /var/atlassian/application-data/jira/dbconfig.xml

cp /opt/quickreload.properties /var/atlassian/application-data/jira/quickreload.properties
cp /opt/quickreload-5.0.6.jar /var/atlassian/application-data/jira/plugins/installed-plugins/quickreload-5.0.6.jar

# commented out because it is not compatible with Jira 10
# cp /opt/jira-data-generator-5.0.0.jar /var/atlassian/application-data/jira/plugins/installed-plugins/jira-data-generator-5.0.0.jar

# TODO remove this line once QuickReload works...
cp /app/scrum-poker.jar /var/atlassian/application-data/jira/plugins/installed-plugins/scrum-poker.jar

chown -R jira:jira /var/atlassian/application-data/jira

/entrypoint.py
