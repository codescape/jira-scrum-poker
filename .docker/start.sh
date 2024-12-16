#!/bin/sh

mkdir -p /var/atlassian/application-data/jira/plugins/installed-plugins

cp /opt/dbconfig.xml /var/atlassian/application-data/jira/dbconfig.xml

cp /opt/quickreload.properties /var/atlassian/application-data/jira/quickreload.properties
cp /opt/quickreload-5.0.6.jar /var/atlassian/application-data/jira/plugins/installed-plugins/quickreload-5.0.6.jar

chown -R jira:jira /var/atlassian/application-data/jira

/entrypoint.py
