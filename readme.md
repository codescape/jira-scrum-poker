# Scrum Poker

This plugin provides a Scrum Poker solution for JIRA that integrates well with the JIRA Agile estimation custom field which is used to calculate the velocity of a sprint.

## Getting Started

The Atlassian Plugin SDK provides several useful commands:

* `atlas-run` installs this plugin into the product and starts it on localhost
* `atlas-debug` same as atlas-run, but allows a debugger to attach at port 5005
* `atlas-cli` after atlas-run or atlas-debug, opens a Maven command line window:
  * `pi` reinstalls the plugin into the running product instance
* `atlas-help` prints description for all commands in the SDK

For further documentation see: [Set up the Atlassian SDK and build a project](https://developer.atlassian.com/docs/getting-started/set-up-the-atlassian-plugin-sdk-and-build-a-project)

## Compatibility

Due to deprecated classes in older versions of the Atlassian SDK, starting with version 1.2.1 Scrum Poker is only compatible to Atlassian JIRA 6.4+.

## Changelog

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
