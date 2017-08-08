# Scrum Poker

This plugin provides a Scrum Poker solution for JIRA that integrates well with the JIRA Agile estimation custom field which is also used to calculate the velocity of a sprint.

[![Build Status](https://travis-ci.org/h4ck4thon/jira-scrum-poker.svg?branch=master)](https://travis-ci.org/h4ck4thon/jira-scrum-poker)

## Getting Started

The Atlassian Plugin SDK provides several useful commands:

* `atlas-run` installs this plugin into the product and starts it on localhost
* `atlas-debug` same as atlas-run, but allows a debugger to attach at port 5005
* `atlas-cli` after atlas-run or atlas-debug, opens a Maven command line window:
  * `pi` reinstalls the plugin into the running product instance
* `atlas-help` prints description for all commands in the SDK

For further documentation see: [Set up the Atlassian SDK and build a project](https://developer.atlassian.com/docs/getting-started/set-up-the-atlassian-plugin-sdk-and-build-a-project)

## Compatibility

Starting with version 1.4.0 the Scrum Poker plugin requires you run Jira 7.3.6 or newer on a Java runtime with Java 8 or newer. 

As a rule of thumb:

* If your Jira is running on Java 7 or older you should use the latest 1.3.16 version.
* If your Jira version is older than Jira 6.4 you should consider updating your Jira instance ;)
* If neither of those two rules applies you can use the latest and greatest version of Scrum Poker!  

## Changelog

### 1.4.1

* cleanup of unused actions and imports, css selectors and code formatting
* improve internal structure of action based code and remove redundancy

### 1.4.0

* implemented using Java 8 language level
* implemented and tested against Jira version 7.3.6

### 1.3.16

* cleanup Scrum Poker Sessions older than 12 hours

### 1.3.15

* Security-Bug: restrict access to configuration to sysadmin role

### 1.3.14

* configuration of story point field is saved by id and not by name anymore (required reconfiguration)
* configuration page includes link to issue tracker on GitHub
* configuration page has alert if no story point field ist configured

### 1.3.13

* Overview page supports refreshing with button

### 1.3.12

* Overview page with all Scrum Poker sessions is accessible from currently opened session
* Overview page with all Scrum Poker sessions shows start date of every Scrum Poker session

### 1.3.11

* add Travis CI build

### 1.3.10

* inject components instead of programmatically getting them
* bounded votes are only exposed through Scrum Poker session now
* overall cleanup (formatting and imports)

### 1.3.9

* fix for URL in session overview

### 1.3.8

* navigate to issue for estimated Scrum Poker sessions in session overview
* new naming for Scrum Poker URLs reflecting the plugin name

### 1.3.7

* Overview page with all currently active Scrum Poker sessions

### 1.3.6

* Improve cleanup of old Scrum Poker sessions

### 1.3.5

* Scrum Poker sessions can be started for all issue types with the configured custom field

### 1.3.4

* Scrum Poker sessions get cleaned up when older than one day
* Participants see confirmed estimation after estimation is confirmed by one participant

### 1.3.3

* Internationalization optimized for German and English users

### 1.3.2

* Scrum Poker session can be opened directly via link URL

### 1.3.1

* Poker card with value 0.5 removed from the Scrum Poker deck

### 1.3.0

* Faster and more responsive interface during Scrum Poker session
