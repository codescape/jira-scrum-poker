# Jira Scrum Poker

Jira Scrum Poker is an open source plugin to enable teams to collaboratively estimate their work with the help of a dedicated GUI directly integrated into Jira Software.

This plugin integrates well with the estimation custom field introduced by Jira Software which is also used to calculate the velocity of a sprint.

[![Build Status](https://travis-ci.org/h4ck4thon/jira-scrum-poker.svg?branch=master)](https://travis-ci.org/h4ck4thon/jira-scrum-poker)

## Screenshots

The configuration page enables the administrator to define the global estimation field.

![Configuration](/etc/screenshots/scrum-poker-configuration.png)

New Scrum Poker sessions can be started from the context menu of any issue with the estimation field or the issue detail page.

![Start Scrum Poker session](/etc/screenshots/scrum-poker-start.png)

The Scrum Poker session page is separated into issue details like summary, description and sub tasks and the Scrum Poker session details like cards, selected estimations and participating users.   

![Running Scrum Poker session](/etc/screenshots/scrum-poker-session.png)

The overview page helps users to find their Scrum Poker session within all currently active and recently closed sessions.

![Active sessions Overview](/etc/screenshots/scrum-poker-active-sessions.png)

## Supported Jira versions

The Scrum Poker plugin requires you to run Jira 7.5 or newer on a Java runtime with Java language level 8.

If your Jira does not meet those expectations you can grab an older version of the Scrum Poker plugin but you will miss all the new features and improvements. 

As a rule of thumb:

* If your Jira is still running on Java 7 you can use Scrum Poker up to version 1.3.16.
* If your Jira version is older than Jira 6.4 you should consider updating your Jira instance ;)
* In all other cases you should use the latest and greatest version of Scrum Poker!

## Supported Languages

The Scrum Poker plugin supports the following languages and defaults to English translation if the user has another language configured in his Jira profile:

* English
* German

## Development

Development of Atlassian Jira plugins is closely bound to the Atlassian Plugin SDK. Setting up your development environment is documented here: [Set up the Atlassian SDK and build a project](https://developer.atlassian.com/docs/getting-started/set-up-the-atlassian-plugin-sdk-and-build-a-project) 

During development you will find the following commands useful:

* `atlas-run` installs this plugin into Jira and starts it on localhost
* `atlas-debug` same as atlas-run, but allows a debugger to attach at port 5005
* `atlas-package` to generate a Jar file of the plugin which can be installed into your Jira instance
* `atlas-clean` to clean up the `target` folder and so allow you to start with a clean Jira instance again

Reloading the plugin during development can be triggered in your web browser when hitting `Shift + Reload` (for example `Shift + Cmd + R` on Mac OS X).
