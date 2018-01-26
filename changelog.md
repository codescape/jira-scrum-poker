# Changelog

This changelog helps developers and users to keep track of new features, fixes and improvements for the Scrum Poker plugin for JIRA.

## Unreleased

* ...

## 1.9.1 - 2018-01-26

* docs: improve documentation for scrum poker session
* docs: fix credits to original author
* docs: fix sample for commit messages
* docs: add section about supported languages
* docs: add section with contribution ideas 
* refactor: simplify verification custom field exists
* refactor: remove unused import
* refactor: log errors when updating issues
* test: verify issue update called after validation

## 1.9.0 - 2017-12-21

* feat: cards are rendered by CSS and replace previously used images
* docs: add contibution guidelines
* fix: show questionmark on backside of cards
* fix: preserve styling for link after click on card
* fix: remove gap between active and closed sessions 

## 1.8.0 - 2017-11-03

* added internationalization for error page
* closed sessions link to issue instead of Scrum Poker session
* sessions overview only shows sessions the user is allowed to see

## 1.7.2 - 2017-10-19

* show closed sessions when no active sessions exist
* improved changelog for better readability

## 1.7.1 - 2017-10-19

* added missing translations for English translation
* correct number of headings for active sessions in session overview

## 1.7.0 - 2017-10-19

* bump version of used JIRA API to version 7.5.0
* improved translations in German and English
* complete internationalization of the plugin in German and English
* layout improvements for page to display active Scrum Poker sessions

## 1.6.1 - 2017-09-08

* provide translation for the active sessions list
* cleanup of plugin configuration
* removed link to missing and unused javascript library

## 1.6.0 - 2017-08-22

* show the name of the user who has started the session in the session list

## 1.5.1 - 2017-08-15

* use base url to display images and links on estimation page

## 1.5.0 - 2017-08-15

* display sub tasks of the story currently being estimated

## 1.4.2 - 2017-08-11

* extract settings handling into on component
* implement against latest Atlassian Maven JIRA Plugin

## 1.4.1 - 2017-08-08

* cleanup of unused actions and imports, css selectors and code formatting
* improve internal structure of action based code and remove redundancy
* closed and open Scrum poker sessions are sorted by creation date

## 1.4.0 - 2017-08-07

* implemented using Java 8 language level
* implemented and tested against JIRA version 7.3.6

## 1.3.16 - 2017-06-21

* cleanup Scrum Poker Sessions older than 12 hours

## 1.3.15 - 2017-02-24

* Security-Bug: restrict access to configuration to sysadmin role

## 1.3.14 - 2017-02-24

* configuration of story point field is saved by id and not by name anymore (required reconfiguration)
* configuration page includes link to issue tracker on GitHub
* configuration page has alert if no story point field ist configured

## 1.3.13 - 2017-02-16

* Overview page supports refreshing with button

## 1.3.12 - 2017-02-13

* Overview page with all Scrum Poker sessions is accessible from currently opened session
* Overview page with all Scrum Poker sessions shows start date of every Scrum Poker session

## 1.3.11 - 2016-08-02

* add Travis CI build

## 1.3.10 - 2016-08-02

* inject components instead of programmatically getting them
* bounded votes are only exposed through Scrum Poker session now
* overall cleanup (formatting and imports)

## 1.3.9 - 2016-07-22

* fix for URL in session overview

## 1.3.8 - 2016-07-22

* navigate to issue for estimated Scrum Poker sessions in session overview
* new naming for Scrum Poker URLs reflecting the plugin name

## 1.3.7 - 2016-07-13

* Overview page with all currently active Scrum Poker sessions

## 1.3.6 - 2016-01-13

* Improve cleanup of old Scrum Poker sessions

## 1.3.5 - 2016-01-08

* Scrum Poker sessions can be started for all issue types with the configured custom field

## 1.3.4 - 2015-11-05

* Scrum Poker sessions get cleaned up when older than one day
* Participants see confirmed estimation after estimation is confirmed by one participant

## 1.3.3 - 2015-11-03

* Internationalization optimized for German and English users

## 1.3.2 - 2015-11-03

* Scrum Poker session can be opened directly via link URL

## 1.3.1 - 2015-11-02

* Poker card with value 0.5 removed from the Scrum Poker deck

## 1.3.0 - 2015-11-02

* Faster and more responsive interface during Scrum Poker session

## 1.2.3 - 2015-05-19

## 1.2.2 - 2015-05-07

## 1.2.1 - 2015-05-05

## 1.2.0 - 2015-04-26

## 1.1.0 - 2015-04-24

## 1.0.0 - 2015-04-23

* Initial usable version of Scrum Poker
