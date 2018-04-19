# Scrum Poker

Scrum Poker is an open source plugin enabling teams to collaboratively estimate their work with the help of an interactive user interface seamlessly integrated into Jira Software.

This plugin works best in combination with the Story Point field introduced by Jira Software which is also used to calculate the sprint velocity.

[![Build Status](https://travis-ci.org/codescape/jira-scrum-poker.svg?branch=master)](https://travis-ci.org/codescape/jira-scrum-poker)

## Highlights

* Easy installation and quick setup in just two steps
* Interactive user interface fostering interaction and conversation
* Simple and quick way of starting new Scrum Poker sessions
* Security and privacy in mind showing only issues users may view
* Free 30 day trial before you need to buy a license

## Installation

Installation and setup of the Scrum Poker plugin from the Atlassian Marketplace is extremely easy: 

1. Install the Scrum Poker plugin from the [Atlassian Marketplace](https://marketplace.atlassian.com/plugins/de.codescape.jira.plugins.scrum-poker). You can start a free trial before purchasing a license for Scrum Poker.  
1. Configure the field to save the confirmed estimation after a Scrum Poker session. To do that go to the plugin configuration and choose the field from the list of possible fields.  

Congratulations! After that you are ready to start your first Scrum Poker session!

## Screenshots

The configuration page enables the Jira administrator to set up the estimation field used by Jira Software to integrate seamlessly.

![Configuration](/etc/screenshots/scrum-poker-configuration.png)

New Scrum Poker sessions can be started from the context menu or from the issue detail page of every issue that has the configured estimation field.

![Start Scrum Poker session](/etc/screenshots/scrum-poker-start.png)

The Scrum Poker session page is separated into issue details like summary, description and sub tasks and the Scrum Poker session details like cards, selected estimations and participating users.   

![Running Scrum Poker session](/etc/screenshots/scrum-poker-session.png)

The overview page helps users to find their Scrum Poker session within all currently active and recently closed sessions.

![Active sessions Overview](/etc/screenshots/scrum-poker-active-sessions.png)

## Compatibility with Jira

The Scrum Poker plugin is compatible with Jira 7.5 and newer versions.

* Scrum Poker 2.4.0 is tested and compatible with Jira 7.5.0 - 7.9.0 

## Supported Languages

The Scrum Poker plugin supports the following languages and defaults to English translation if the user has no supported language configured in his Jira profile:

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
