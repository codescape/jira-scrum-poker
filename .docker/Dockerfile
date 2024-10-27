FROM atlassian/jira-software:latest

RUN apt-get clean -y && apt-get update -y && apt-get install postgresql-client -y;

# COPY ./dbconfig.xml ./jira-data-generator-5.0.0.jar ./quickreload-5.0.6.jar ./quickreload.properties /opt/
COPY ./dbconfig.xml ./quickreload-5.0.6.jar ./quickreload.properties /opt/

# TODO remove once QuickReload works
COPY ./scrum-poker.jar /app/

COPY ./start.sh /opt/start.sh

CMD /opt/start.sh
