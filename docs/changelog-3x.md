---
layout: default
title: Changelog 3.x
category: Administration
---

{% include changelog-preface.md %}

* Table of Contents
{:toc}

### [3.13.0] - 2019-04-22

**Certified support and compatibility for Jira Data Center**

*This version was not published on Atlassian Marketplace because of a pending approval for Jira Data Center.*

* docs: change order of highlights in readme
* feat: migrate old votes with question mark to new string format
* test: improve test coverage and suppress unused warnings
* test: include performance test suite for Jira Data Center
* test: add markers to session list and detail view to identify pages
* feat: copy session link with a button on the share session screen (#62)
* feat: document Jira Data Center compatibility in plugin descriptor
* style: format css code consistently
* feat: prevent images in issue description from floating into poker elements
* feat: display dates in user specific date format (#57)
* feat: explain how to start new sessions on empty active session list

### [3.12.0] - 2019-04-05

**Improved styling for break and question cards and compatibility with Jira 8.1.x**

* feat: signal that a break is needed only when deck is revealed
* docs: describe possible usage of test groups
* feat: use Font Awesome to display icons for coffee card and question mark
* chore: implement against latest Jira patch version 8.1.0
* chore: update tested patch version of Jira 7.6 enterprise release
* docs: improve compatibility matrix in readme

### [3.11.1] - 2019-04-01

**Compatibility fix for Jira 7.x in Scrum Poker 3.11.0**

* docs: move changelog into online documentation
* docs: improve changelog with additional description
* fix: do not rely on commons-lang 3.5 for checking numeric values
* test: add test groups for supported versions of Jira (#55)

### [3.11.0] - 2019-03-30

**Introducing coffee card for breaks and improved estimation confirmation screen**

* chore: use short name Scrum Poker inside Jira GUI
* style: format configuration files
* docs: update docs according to usage of the short name
* refactor: remove usage of deprecated API and switch to new method
* fix: IntelliJ inspection results
* docs: add section for Maven dependency analysis
* chore: cleanup dependencies and remove unnecessary dependencies
* chore: explicitly reference required dependencies
* chore: re-establish quick reload functionality
* feat: update german translation to fit to Jira internal wording
* chore: non verbose logging on maven lifecycle
* test: remove unused mocks from test and clean up test logging
* docs: use long name in title of online documentation
* docs: add documentation to all action classes
* chore: implement against latest Jira patch version 8.0.2
* feat: persist and show user and date when confirming an estimation (#56)
* docs: improve headings and navigation in online documentation
* feat: introduce a coffee card to signal that a participant needs a break (#13)

### [3.10.0] - 2019-02-14

**Scrum Poker for Jira now supports Jira 8**

* chore: implement against latest Jira version
* test: improve tests for validity of message bundles for internationalization
* feat: implement with compatibility for Jira 8.0.0
* chore: update pom documentation and remove not required import packages
* chore: use Scrum Poker for Jira as new name and refer to it as a Jira app
* docs: update compatibility matrix

### [3.9.0] - 2019-02-01

**Improved button styling, new configuration options and French translation**

* feat: improve button styling to refresh session list
* feat: improve button styling for Scrum Poker session page
* chore: update dependency versions of all used libraries
* feat: show number of votes during Scrum Poker session
* feat: add global configuration to define who is allowed to reveal the deck
* fix: display number of votes correctly for more than one participant
* feat: vertically align buttons with text on buttons
* feat: add complete internationalization of the plugin in French

### [3.8.0] - 2019-01-14

**Configure Scrum Poker per project and share Scrum Poker sessions easily**

* docs: provide EULA and privacy statement in online documentation
* feat: add global configuration to enable or disable Scrum Poker per default
* feat: add per project configuration to enable Scrum Poker individually
* chore: upgrade spring and lucene dependencies to latest stable version
* docs: add online documentation for project specific configuration
* refactor: improve readability and structure of all action classes
* refactor: rename and move transformer classes into separate mapper package
* chore: update URL for Atlassian Maven repository to new non deprecated URL
* refactor: correct spelling for timeout in interface an implementation
* refactor: build Atlassian plugin coordinates in Maven POM and use in plugin
* refactor: reorder web-item parameters in configuration
* feat: add share option to scan QR code or copy link to Scrum Poker session
* refactor: cleanup of Javascript code for QR code and copy link initialization
* docs: add contact mail on start page of online documentation
* docs: add call to action button to download and install Scrum Poker
* docs: document process of sharing a Scrum Poker session via URL and QR code

### [3.7.0] - 2018-12-03

**Compatibility with Jira 7.13**

* test: cleanup test for REST endpoint and remove unnecessary test data
* test: cleanup test for automatic upgrade task and remove unnecessary test data
* test: improve naming of test methods to better reflect test case
* feat: improve error page and error massage in case of an error
* feat: automatically install Jira Software in development mode
* refactor: retrieve current user in separate and reusable method
* chore: implement against latest version of Atlassian SDK
* test: add test for correct behaviour when cancelling a session
* chore: implement against latest Jira patch version 7.13.0

### [3.6.0] - 2018-09-26

**New styling for session page and badges with estimation distribution count**

* chore: upgrade all dependencies to latest stable version
* feat: adapt Jira style guide for Scrum Poker card layout
* refactor: use Spring annotations and conform to atlassian-spring-scanner
* docs: update inline documentation in POM file
* test: add tests for correct format of message bundles for language support
* docs: add Java documentation for developers to all Active Objects
* test: add tests for automatic migration of old configuration
* refactor: reduce class visibility and add final to constructor assigned fields
* feat: add logging for automatic plugin updates
* feat: use transactions for writing Scrum Poker data (#32)
* feat: show badge with number of votes for every card when cards are revealed
* refactor: format session detail view page
* docs: update inline documentation for style sheets
* docs: add screenshots for online documentation
* docs: add online documentation for the Scrum Poker process
* docs: simplify readme and remove unused screenshots
* refactor: optimize imports and format code and configuration

### [3.5.0] - 2018-09-16

**Readiness for Data Center with user interface improvements**

* docs: improve online documentation layout for mobile devices
* feat: make Scrum Poker accessible from Jira Software boards menu
* docs: update documentation for joining an active session
* refactor: use links starting with uppercase for Scrum Poker
* feat: move Scrum Poker configuration into database (#31)
* test: simplify test and remove unnecessary test data
* feat: add parameter to signal Atlassian Data Center compatibility (#24)
* feat: automatically migrate old configuration into new one and cleanup
* feat: layout improvements for Scrum Poker session to better guide users

### [3.4.0] - 2018-09-15

**Improved Scrum Poker session page and new online documentation**

* fix: correct escape for umlauts in configuration description
* chore: implement against latest Jira version
* refactor: rename Scrum Poker session service implementation
* feat: prevent issue description from floating into poker elements
* refactor: change primary key for persisted votes from int to Long
* feat: do not create Scrum Poker sessions for issues missing estimation field
* test: use Mockito ArgumentMatcher instead of deprecated Mockito Matcher
* test: improve tests and remove unnecessary details in setup
* chore: implement against latest Jira patch version 7.12.1
* feat: sort buttons on Scrum Poker session page by usage frequency
* docs: implement a first version of an online documentation

### [3.3.0] - 2018-09-01

**Improved process of saving estimations to issues in backend and GUI**

* feat: ensure minimum value of zero for session timeout configuration
* feat: show warning/success message when saving estimation to issue
* docs: add inline documentation to CSS code
* feat: improve error message when saving estimation to issue
* test: improve log output for better readability

### [3.2.0] - 2018-08-10

**Scrum Poker sessions can be cancelled by the session starter**

* feat: Scrum Poker session timeout configurable in plugin settings (#19)
* feat: allow the creator of a session to cancel the session (#20)
* chore: implement against latest Jira patch version 7.11.2
* fix: button "Start Scrum Poker" is shown twice (#28)
* feat: improve readability of global plugin configuration
* refactor: use Java 8 lambdas and use null save operations where appropriate

### [3.1.2] - 2018-07-27

**Improvements to reference issues and active sessions list**

* docs: improve privacy statement with details to data usage
* fix: cancel fading out reference issues when references shall be displayed
* fix: hide references immediately when estimation is confirmed
* fix: do not show references where issue does not exist anymore
* chore: implement against latest Jira patch version 7.11.1
* feat: simplify add-on global configuration page

### [3.1.1] - 2018-07-18

**Fixes for robustness of active session list**

* fix: do not fail to display session list for not existing issues
* fix: do not create sessions for not existing issues
* feat: reduce requests to reference issues and fade out slowly in gui
* refactor: improve code for persistence service

### [3.1.0] - 2018-07-17

**Show reference issues during session to help find the estimation**

* feat: show reference issues during session to help find the estimation
* refactor: improve code to transform database model into the view model
* refactor: rename estimation field service and use dependency injection
* refactor: rename settings service and use dependency injection

### [3.0.1] - 2018-07-16

**Fixes for correct visualization of Scrum Poker sessions**

* chore: implement against Jira version 7.11.x
* fix: do announce agreement only for revealed sessions
* fix: hide the revealed session when new votes are added (#23)

### [3.0.0] - 2018-07-13

**Complete rewrite of the persistence layer to persist Scrum Poker sessions**

* chore: implement against latest Jira version 7.10.2
* test: add tests for Scrum Poker configuration action
* refactor: rename and move condition to display Scrum Poker for issue
* refactor: use better base class for condition to render Scrum Poker button
* feat: add link to marketplace on plugin configuration page
* refactor: introducing persistence to Scrum Poker session with Active Objects

[3.13.0]: https://github.com/codescape/jira-scrum-poker/compare/3.12.0...3.13.0
[3.12.0]: https://github.com/codescape/jira-scrum-poker/compare/3.11.1...3.12.0
[3.11.1]: https://github.com/codescape/jira-scrum-poker/compare/3.11.0...3.11.1
[3.11.0]: https://github.com/codescape/jira-scrum-poker/compare/3.10.0...3.11.0
[3.10.0]: https://github.com/codescape/jira-scrum-poker/compare/3.9.0...3.10.0
[3.9.0]: https://github.com/codescape/jira-scrum-poker/compare/3.8.0...3.9.0
[3.8.0]: https://github.com/codescape/jira-scrum-poker/compare/3.7.0...3.8.0
[3.7.0]: https://github.com/codescape/jira-scrum-poker/compare/3.6.0...3.7.0
[3.6.0]: https://github.com/codescape/jira-scrum-poker/compare/3.5.0...3.6.0
[3.5.0]: https://github.com/codescape/jira-scrum-poker/compare/3.4.0...3.5.0
[3.4.0]: https://github.com/codescape/jira-scrum-poker/compare/3.3.0...3.4.0
[3.3.0]: https://github.com/codescape/jira-scrum-poker/compare/3.2.0...3.3.0
[3.2.0]: https://github.com/codescape/jira-scrum-poker/compare/3.1.2...3.2.0
[3.1.2]: https://github.com/codescape/jira-scrum-poker/compare/3.1.1...3.1.2
[3.1.1]: https://github.com/codescape/jira-scrum-poker/compare/3.1.0...3.1.1
[3.1.0]: https://github.com/codescape/jira-scrum-poker/compare/3.0.1...3.1.0
[3.0.1]: https://github.com/codescape/jira-scrum-poker/compare/3.0.0...3.0.1
[3.0.0]: https://github.com/codescape/jira-scrum-poker/compare/2.8.0...3.0.0
