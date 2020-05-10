---
layout: default
title: Changelog 1.x
---

{% include changelog-preface.md %}

* Table of Contents
{:toc}

### [1.12.0] - 2018-03-11

**New Scrum Poker sessions only for estimable items and much cleaner session overview**

* refactor: implement choose card action with testability in mind
* feat: non editable issues do not display Scrum Poker button
* docs: rename JIRA to Jira in all places in the code
* chore: rename plugin to Scrum Poker and add better description
* chore: implement with latest amps and Jira version
* refactor: Java 8 way to create and return session
* style: restructure Maven POM
* feat: remove timed out sessions also in session overview
* feat: improve layout and wording for session view
* refactor: use Integer for estimations everywhere
* chore: update version of amps to 6.3.13

### [1.11.0] - 2018-02-27

**Permission check so that no issues are shown to unprivileged users**

* feat: stop users from opening issues in Scrum Poker they may not see
* feat: add robustness when a key cannot be resolved to a user
* feat: improve error page which will now also appear when user is not allowed to see issue
* refactor: simplify refreshing session data and remove unused code
* docs: fix typo and remove trailing whitespace
* style: add initial version of EditorConfig file
* style: add indent and charset to EditorConfig and apply

### [1.10.0] - 2018-02-02

**Navigation for participants after confirmed estimation is shown**

* feat: remove reset button from confirmed estimation page
* feat: add active sessions link on confirmed estimation page
* feat: add return to issue link on confirmed estimation page
* feat: improve wording for confirmed estimation page
* style: use white space instead of tabs for templates

### [1.9.1] - 2018-01-26

**Log error when updating an issue fails with an error**

* docs: improve documentation for scrum poker session
* docs: fix credits to original author
* docs: fix sample for commit messages
* docs: add section about supported languages
* docs: add section with contribution ideas
* refactor: simplify verification custom field exists
* refactor: remove unused import
* refactor: log errors when updating issues
* test: verify issue update called after validation

### [1.9.0] - 2017-12-21

**Display Scrum Poker cards with CSS replacing previously used images**

* feat: cards are rendered by CSS and replace previously used images
* docs: add contibution guidelines
* fix: show questionmark on backside of cards
* fix: preserve styling for link after click on card
* fix: remove gap between active and closed sessions

### [1.8.0] - 2017-11-03

**Session overview shows issues based on user permissions**

* feat: added internationalization for error page
* feat: closed sessions link to issue instead of Scrum Poker session
* feat: sessions overview only shows sessions the user is allowed to see

### [1.7.2] - 2017-10-19

**Fixing session overview to display closed sessions even when no active sessions exist**

* fix: show closed sessions when no active sessions exist
* docs: improve changelog for better readability

### [1.7.1] - 2017-10-19

**Fixing missing translations on session overview**

* fix: add missing translations for English translation
* fix: correct number of headings for active sessions in session overview

### [1.7.0] - 2017-10-19

**Internationalization for all Scrum Poker pages and texts**

* feat: bump version of used Jira API to version 7.5.0
* feat: improve translations in German and English
* feat: complete internationalization of the plugin in German and English
* feat: layout improvements for page to display active Scrum Poker sessions

### [1.6.1] - 2017-09-08

**Internationalization for the active session page**

* feat: provide translation for the active sessions list
* chore: cleanup of plugin configuration
* chore: removed link to missing and unused javascript library

### [1.6.0] - 2017-08-22

**Display the name of the session starter in the session list**

* feat: show the name of the user who has started the session in the session list

### [1.5.1] - 2017-08-15

**Fixing the URL to images and resources for Jira installations without context path**

* fix: use base url to display images and links on estimation page

### [1.5.0] - 2017-08-15

**Display all sub tasks for the current session if sub tasks exist**

* feat: display sub tasks of the story currently being estimated

### [1.4.2] - 2017-08-11

**Update add on using the latest Atlassian SDK**

* refactor: extract settings handling into one component
* feat: implement against latest Atlassian Maven Jira Plugin

### [1.4.1] - 2017-08-08

**Sort active and recently closed Scrum Poker sessions by creation date**

* chore: cleanup of unused actions and imports, css selectors
* style: improve code formatting
* refactor: improve internal structure of action based code and remove redundancy
* feat: closed and open Scrum Poker sessions are sorted by creation date

### [1.4.0] - 2017-08-07

**Support for Atlassian Jira 7.3.6**

* refactor: use Java 8 language level
* feat: implement and test against Jira version 7.3.6

### [1.3.16] - 2017-06-21

**Remove Scrum Poker sessions after the configured timeout**

* feat: cleanup Scrum Poker Sessions older than 12 hours

### [1.3.15] - 2017-02-24

**Fixing access to the configuration to require a user with sysadmin role**

* fix: restrict access to configuration to sysadmin role

### [1.3.14] - 2017-02-24

**Improve configuration page to have valid and working configurations**

* feat: configuration of story point field is saved by id and not by name anymore (requires reconfiguration)
* feat: configuration page includes link to issue tracker on GitHub
* feat: configuration page has alert if no story point field ist configured

### [1.3.13] - 2017-02-16

**Allow to easily refresh the active session page**

* feat: overview page supports refreshing with button

### [1.3.12] - 2017-02-13

**Access the active session list from every Scrum Poker session**

* feat: overview page with all Scrum Poker sessions is accessible from currently opened session
* feat: overview page with all Scrum Poker sessions shows start date of every Scrum Poker session

### [1.3.11] - 2016-08-02

**Improve testing and include continuous integration system**

* chore: add Travis CI build

### [1.3.10] - 2016-08-02

**Internally optimize access to Jira components**

* refactor: inject components instead of programmatically getting them
* refactor: bounded votes are only exposed through Scrum Poker session now
* style: overall cleanup (formatting and imports)

### [1.3.9] - 2016-07-22

**Bugfix for wrong hyperlinks on session overview page**

* fix: display correct URL in session overview

### [1.3.8] - 2016-07-22

**Open issue instead of session on sessions with estimation already agreed on**

* feat: navigate to issue for estimated Scrum Poker sessions in session overview
* refactor: new naming for Scrum Poker URLs reflecting the plugin name

### [1.3.7] - 2016-07-13

**First version of active session page**

* feat: add overview page with all currently active Scrum Poker sessions

### [1.3.6] - 2016-01-13

**Fix for error during parallel cleanup of old Scrum Poker sessions**

* refactor: improve cleanup of old Scrum Poker sessions

### [1.3.5] - 2016-01-08

**Scrum Poker sessions can be started for all issue types**

* feat: Scrum Poker sessions can be started for all issue types with the configured custom field

### [1.3.4] - 2015-11-05

**Old Scrum Poker sessions older than one day are cleaned up**

* feat: Scrum Poker sessions get cleaned up when older than one day
* feat: Participants see confirmed estimation when confirmed by one participant

### [1.3.3] - 2015-11-03

**Multi-language support for German and English users**

* feat: optimize internationalization for German and English users

### [1.3.2] - 2015-11-03

**Scrum Poker session url can be shared by users**

* feat: open new Scrum Poker sessions via URL

### [1.3.1] - 2015-11-02

**Fix: Poker card 0.5 removed as it cannot be saved**

* feat: poker card with value 0.5 removed from the Scrum Poker deck

### [1.3.0] - 2015-11-02

* feat: faster and more responsive interface during Scrum Poker session

### [1.2.3] - 2015-05-19

* feat: automatic refresh of poker cards in session every two seconds
* docs: added section with compatibility matrix in readme

### [1.2.2] - 2015-05-07

* refactor: extract votes logic into separate class and add tests
* feat: better approach to recognize referrer and lead back after poker session

### [1.2.1] - 2015-05-05

* feat: implement and test against Jira 6.4.3
* feat: rename Planning Poker Plugin to Scrum Poker Plugin
* refactor: simplify card image strategy using folders instead of file names
* refactor: introduce ScrumPokerAction as base for all action classes
* refactor: use IssueService instead of deprecated IssueManager
* refactor: rename configuration action according to other action classes
* style: use spaces instead of tabs and remove blank lines
* docs: add Apache License Version 2.0
* docs: improve readme and add description of the plugin
* docs: add javadoc to PlanningPokerStorage interface
* chore: add ignored file patterns for git version control
* chore: add Atlassian repository in POM to resolve Atlassian dependencies

### [1.2.0] - 2015-04-26

* fix: save and refresh estimation to be displayed in product backlog

### [1.1.0] - 2015-04-24

* feat: rename Planning Poker Plugin to Scrum Poker Plugin
* feat: reveal deck only if there are no new votes coming in
* feat: identify people with votes on the boundaries to engage talking to each other
* feat: allow resetting a deck after a Scrum Poker session
* feat: allow opening Scrum Poker from issue sidebar in Agile Board
* feat: add localization for German language
* feat: add filter to show and hide the Scrum Poker button based on issue types
* fix: single logic for naming the Scrum Poker estimations field
* fix: more robustness for filter to show and hide the Scrum Poker button based on issue types
* fix: encoding of German umlauts in German message bundle
* fix: invite to talk only when there are minimum two different estimations
* style: cleanup and formatting of initially generated POM
* style: global optimizations regarding formatting and code style

### [1.0.0] - 2015-04-23

**First runnable version of Scrum Poker**

* feat: initial usable version of Planning Poker Plugin

[1.12.0]: https://github.com/codescape/jira-scrum-poker/compare/1.11.0...1.12.0
[1.11.0]: https://github.com/codescape/jira-scrum-poker/compare/1.10.0...1.11.0
[1.10.0]: https://github.com/codescape/jira-scrum-poker/compare/1.9.1...1.10.0
[1.9.1]: https://github.com/codescape/jira-scrum-poker/compare/1.9.0...1.9.1
[1.9.0]: https://github.com/codescape/jira-scrum-poker/compare/1.8.0...1.9.0
[1.8.0]: https://github.com/codescape/jira-scrum-poker/compare/1.7.2...1.8.0
[1.7.2]: https://github.com/codescape/jira-scrum-poker/compare/1.7.1...1.7.2
[1.7.1]: https://github.com/codescape/jira-scrum-poker/compare/1.7.0...1.7.1
[1.7.0]: https://github.com/codescape/jira-scrum-poker/compare/1.6.1...1.7.0
[1.6.1]: https://github.com/codescape/jira-scrum-poker/compare/1.6.0...1.6.1
[1.6.0]: https://github.com/codescape/jira-scrum-poker/compare/1.5.1...1.6.0
[1.5.1]: https://github.com/codescape/jira-scrum-poker/compare/1.5.0...1.5.1
[1.5.0]: https://github.com/codescape/jira-scrum-poker/compare/1.4.2...1.5.0
[1.4.2]: https://github.com/codescape/jira-scrum-poker/compare/1.4.1...1.4.2
[1.4.1]: https://github.com/codescape/jira-scrum-poker/compare/1.4.0...1.4.1
[1.4.0]: https://github.com/codescape/jira-scrum-poker/compare/1.3.16...1.4.0
[1.3.16]: https://github.com/codescape/jira-scrum-poker/compare/1.3.15...1.3.16
[1.3.15]: https://github.com/codescape/jira-scrum-poker/compare/1.3.14...1.3.15
[1.3.14]: https://github.com/codescape/jira-scrum-poker/compare/1.3.13...1.3.14
[1.3.13]: https://github.com/codescape/jira-scrum-poker/compare/1.3.12...1.3.13
[1.3.12]: https://github.com/codescape/jira-scrum-poker/compare/1.3.11...1.3.12
[1.3.11]: https://github.com/codescape/jira-scrum-poker/compare/1.3.10...1.3.11
[1.3.10]: https://github.com/codescape/jira-scrum-poker/compare/1.3.9...1.3.10
[1.3.9]: https://github.com/codescape/jira-scrum-poker/compare/1.3.8...1.3.9
[1.3.8]: https://github.com/codescape/jira-scrum-poker/compare/1.3.7...1.3.8
[1.3.7]: https://github.com/codescape/jira-scrum-poker/compare/1.3.6...1.3.7
[1.3.6]: https://github.com/codescape/jira-scrum-poker/compare/1.3.5...1.3.6
[1.3.5]: https://github.com/codescape/jira-scrum-poker/compare/1.3.4...1.3.5
[1.3.4]: https://github.com/codescape/jira-scrum-poker/compare/1.3.3...1.3.4
[1.3.3]: https://github.com/codescape/jira-scrum-poker/compare/1.3.2...1.3.3
[1.3.2]: https://github.com/codescape/jira-scrum-poker/compare/1.3.1...1.3.2
[1.3.1]: https://github.com/codescape/jira-scrum-poker/compare/1.3.0...1.3.1
[1.3.0]: https://github.com/codescape/jira-scrum-poker/compare/1.2.3...1.3.0
[1.2.3]: https://github.com/codescape/jira-scrum-poker/compare/1.2.2...1.2.3
[1.2.2]: https://github.com/codescape/jira-scrum-poker/compare/1.2.1...1.2.2
[1.2.1]: https://github.com/codescape/jira-scrum-poker/compare/1.2.0...1.2.1
[1.2.0]: https://github.com/codescape/jira-scrum-poker/compare/1.1.0...1.2.0
[1.1.0]: https://github.com/codescape/jira-scrum-poker/compare/1.0.0...1.1.0
[1.0.0]: https://github.com/codescape/jira-scrum-poker/tree/1.0.0
