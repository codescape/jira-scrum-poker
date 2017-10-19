# Changelog

This changelog helps developers and users to keep track of new features, fixes and improvements for the Scrum Poker plugin for JIRA.

## 1.7.0 ()

* bump version of JIRA API to version 7.5.0
* cleanup and improvements for translations in German and English

## 1.6.1 (08.09.2017)

* provide translation for the active sessions list
* cleanup of plugin configuration
* removed link to missing and unused javascript library 

## 1.6.0 (22.08.2017)

* show the name of the user who has started the session in the session list 

## 1.5.1 (15.08.2017)

* use base url to display images and links on estimation page

## 1.5.0 (15.08.2017)

* display sub tasks of the story currently being estimated

## 1.4.2 (11.08.2017)

* extract settings handling into on component
* implement against latest Atlassian Maven JIRA Plugin

## 1.4.1 (08.08.2017)

* cleanup of unused actions and imports, css selectors and code formatting
* improve internal structure of action based code and remove redundancy
* closed and open Scrum poker sessions are sorted by creation date 

## 1.4.0 (07.08.2017)

* implemented using Java 8 language level
* implemented and tested against JIRA version 7.3.6

## 1.3.16 (21.06.2017)

* cleanup Scrum Poker Sessions older than 12 hours

## 1.3.15 (24.02.2017)

* Security-Bug: restrict access to configuration to sysadmin role

## 1.3.14 (24.02.2017)

* configuration of story point field is saved by id and not by name anymore (required reconfiguration)
* configuration page includes link to issue tracker on GitHub
* configuration page has alert if no story point field ist configured

## 1.3.13 (16.02.2017)

* Overview page supports refreshing with button

## 1.3.12 (13.02.2017)

* Overview page with all Scrum Poker sessions is accessible from currently opened session
* Overview page with all Scrum Poker sessions shows start date of every Scrum Poker session

## 1.3.11 (02.08.2016)

* add Travis CI build

## 1.3.10 (02.08.2016)

* inject components instead of programmatically getting them
* bounded votes are only exposed through Scrum Poker session now
* overall cleanup (formatting and imports)

## 1.3.9 (22.07.2016)

* fix for URL in session overview

## 1.3.8 (22.07.2016)

* navigate to issue for estimated Scrum Poker sessions in session overview
* new naming for Scrum Poker URLs reflecting the plugin name

## 1.3.7 (15.07.2016)

* Overview page with all currently active Scrum Poker sessions

## 1.3.6 (13.01.2016)

* Improve cleanup of old Scrum Poker sessions

## 1.3.5 (08.01.2016)

* Scrum Poker sessions can be started for all issue types with the configured custom field

## 1.3.4 (05.05.2015)

* Scrum Poker sessions get cleaned up when older than one day
* Participants see confirmed estimation after estimation is confirmed by one participant

## 1.3.3 (03.11.2015)

* Internationalization optimized for German and English users

## 1.3.2 (03.11.2015)

* Scrum Poker session can be opened directly via link URL

## 1.3.1 (02.11.2015)

* Poker card with value 0.5 removed from the Scrum Poker deck

## 1.3.0 (02.11.2015)

* Faster and more responsive interface during Scrum Poker session

## 1.2.3 (19.05.2015)

## 1.2.2 (07.05.2015)

## 1.2.1 (05.05.2015)

## 1.2.0 (26.04.2015)

## 1.1.0 (24.04.2015)

## 1.0.0 (23.04.2015)

* Initial usable version of Scrum Poker
