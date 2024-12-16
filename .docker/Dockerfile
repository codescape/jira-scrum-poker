FROM atlassian/jira-software:latest

RUN apt-get clean -y && apt-get update -y && apt-get install postgresql-client -y;

COPY ./dbconfig.xml ./quickreload-5.0.6.jar ./quickreload.properties /opt/

COPY ./start.sh /opt/start.sh

CMD /opt/start.sh
