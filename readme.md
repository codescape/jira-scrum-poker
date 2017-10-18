# Scrum Poker

This plugin provides a Scrum Poker solution for JIRA that integrates well with the JIRA Agile estimation custom field which is also used to calculate the velocity of a sprint.

[![Build Status](https://travis-ci.org/h4ck4thon/jira-scrum-poker.svg?branch=master)](https://travis-ci.org/h4ck4thon/jira-scrum-poker)

## Screenshots

![Configuration](/etc/screenshots/scrum-poker-configuration.png)

![Start Scrum Poker session](/etc/screenshots/scrum-poker-start.png)

![Running Scrum Poker session](/etc/screenshots/scrum-poker-session.png)

![Active sessions Overview](/etc/screenshots/scrum-poker-active-sessions.png)

## Compatibility

Starting with version 1.4.0 the Scrum Poker plugin requires you to run Jira 7.3.6 or newer on a Java runtime with Java 8 or newer. 

As a rule of thumb:

* If your Jira is running on Java 7 or older you should use the latest 1.3.16 version.
* If your Jira version is older than Jira 6.4 you should consider updating your Jira instance ;)
* If neither of those two rules applies you can use the latest and greatest version of Scrum Poker!  

## Development

Development of Atlassian JIRA plugins is closely bound to the Atlassian Plugin SDK. Setting up your development environment is documented here: [Set up the Atlassian SDK and build a project](https://developer.atlassian.com/docs/getting-started/set-up-the-atlassian-plugin-sdk-and-build-a-project) 

During development you will find the following commands useful:

* `atlas-run` installs this plugin into the product and starts it on localhost
* `atlas-debug` same as atlas-run, but allows a debugger to attach at port 5005
