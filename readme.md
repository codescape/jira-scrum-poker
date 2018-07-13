# Scrum Poker

Scrum Poker is an open source plugin enabling teams to collaboratively estimate their work with the help of an interactive user interface seamlessly integrated into Jira Software.

This plugin works best in combination with the Story Point field introduced by Jira Software which is also used to calculate the sprint velocity.

[![Build Status](https://travis-ci.org/codescape/jira-scrum-poker.svg?branch=master)](https://travis-ci.org/codescape/jira-scrum-poker)

## Highlights

* Easy installation and quick setup in just two steps
* Interactive user interface fostering interaction and conversation
* Simple and quick way of starting new Scrum Poker sessions
* Security and privacy in mind showing only issues users may view

## Installation

Installation and setup of the Scrum Poker plugin from the Atlassian Marketplace is extremely easy: 

1. Install the Scrum Poker plugin from the [Atlassian Marketplace](https://marketplace.atlassian.com/plugins/de.codescape.jira.plugins.scrum-poker).
1. Configure the field to save the confirmed estimation after a Scrum Poker session. To do that go to the plugin configuration and choose the field from the list of possible fields.  

Congratulations! After that you are ready to start your first Scrum Poker session!

## Screenshots

The configuration page enables the Jira administrator to set up the estimation field used by Jira Software to integrate seamlessly.

![Configuration](/etc/screenshots/scrum-poker-configuration.png)

New Scrum Poker sessions can be started from the context menu or from the issue detail page of every issue that has the configured estimation field.

![Start Scrum Poker session](/etc/screenshots/scrum-poker-start.png)

The Scrum Poker session page is separated into issue details like summary, description and sub tasks and the Scrum Poker session details like cards, selected estimations and participating users.   

![Providing estimates by all participants](/etc/screenshots/scrum-poker-session-hidden.png)

When the Scrum Poker session is revealed all estimations will be shown and to help the team finding a common estimation participants with estimations on the boundaries are advised to talk to each other.

![Discussion about the bandwidth of estimates](/etc/screenshots/scrum-poker-session-revealed.png)

The overview page helps users to find their Scrum Poker session within all currently active and recently closed sessions.

![Active sessions Overview](/etc/screenshots/scrum-poker-active-sessions.png)

## Compatibility with Jira

The Scrum Poker plugin is compatible with Jira 7.5.0 and newer versions.

We always try to develop the Scrum Poker plugin against the latest version of Jira to ensure support for new versions continuously.

| Scrum Poker version | Jira version |
|---------------------|--------------|
| 1.11.0              | 7.5.x        |
| 1.12.0              | 7.8.x        |
| 2.5.0               | 7.9.x        |
| 3.0.0               | 7.10.x       |

## Supported Languages

The Scrum Poker plugin supports the following languages and defaults to English translation if the user has no supported language configured in his Jira profile:

* English
* German
