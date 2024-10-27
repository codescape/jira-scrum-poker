# How to run the app on Docker

_Credits go to https://github.com/collabsoft-net/example-jira-app-with-docker-compose_

The file `docker-compose.yml` and the `.docker` folder are required to run the plugin with Docker.

To start the Jira instance and the database:

```
docker-compose up -d
```

Wait 1-2 minute(s) and open http://localhost:8080.
You will need to go through the Jira set-up process and provide a valid license. 
Configure the application as desired. Once you've finished configuring Jira, you can install the app (and any updates) by running

```
atlas-mvn package
```

This will create a new JAR file in `./target` which will be picked up by QuickReload and installed in your Jira instance.

## Debugging your app

The Jira instance is started with JVM debugging enabled. You can connect your IDE to remote debugging on port 5005

In addition, you can check the Atlassian Jira application log by running

```
docker-compose exec jira tail -f -n 5000 /var/atlassian/application-data/jira/log/atlassian-jira.log
```

Copy the whole log file from the container:

```
docker-compose cp jira:/var/atlassian/application-data/jira/log/atlassian-jira.log ./atlassian-jira.log
```
