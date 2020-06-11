---
layout: default
title: Changelog 2.x
category: Administration
---

{% include changelog-preface.md %}

* Table of Contents
{:toc}

### [2.8.0] - 2018-06-01

**Scrum Poker is now provided for free on Atlassian Marketplace**

* feat: Scrum Poker is now provided for free on Atlassian Marketplace

### [2.7.0] - 2018-05-17

**Better visualization of sub task list in Scrum Poker session**

* fix: remove small typo in privacy statement
* feat: show display name of assignee in list of sub tasks
* feat: display the list of sub tasks equally to the issue detail page
* chore: use latest version von Jira and Lucene

### [2.6.0] - 2018-05-13

**Further improvements to graphical user interface and REST backend**

* feat: use buttons and Jira default styling where appropriate
* docs: changelog includes links to GitHub comparison between tags
* docs: improve Credits section in Contributing document
* docs: add Code of Conduct file
* docs: add Releases section in Contributing documentation
* feat: less text same meaning for session list headings
* docs: provide better screenshots and show more situations
* refactor: move REST entities into separate package
* refactor: rename REST entities to \*Entity
* refactor: use Jackson annotations for JSON entity classes
* docs: provide Privacy Statement for Scrum Poker

### [2.5.0] - 2018-04-29

**Optimized user interface for running Scrum Poker sessions**

* chore: implement against latest amps and Jira version
* docs: update and improve compatibility description
* chore: upgrade all dependencies to latest stable version
* style: format members of all classes
* test: improve readability of tests
* docs: add Javadoc for ScrumPokerCard class
* docs: add Javadoc to all production classes
* test: add tests for REST data representations
* refactor: extract issue permission verification for current user
* fix: remove typo in configuration text
* feat: agreement is not reached for only one vote
* refactor: improve code for indicator that talking is required
* feat: show clickable indicator only for clickable cards
* feat: optimize session screen for better readability and usability
* feat: improve wording on session screen

### [2.4.1] - 2018-04-20

**Fixing the confirmation screen for confirmed votes of 0**

* fix: estimation of 0 can be confirmed correctly
* refactor: improved naming convention for REST representations
* docs: update and improve general description of the Scrum Poker plugin
* docs: update and improve installation and compatibility description

### [2.4.0] - 2018-04-16

**Improved client library for Scrum Poker sessions**

* feat: add post update and post install urls to improve setup process
* refactor: move all persistence related classes into persistence package
* feat: create own ScrumPoker namespace for JavaScript code
* docs: improve readme and add installation instructions
* style: formatting the JavaScript file
* feat: rename to Scrum Poker to adhere to Atlassian brand guidelines

### [2.3.0] - 2018-04-11

**Configuration accessible from plugin administration sidebar**

* style: tabs and spaces for session template
* docs: update screenshots and description in readme
* feat: configuration accessible from plugin administration sidebar
* feat: remove logging while accessing story point field
* feat: introduce licensing for the Scrum Poker plugin
* refactor: introduce better naming for plugin components

### [2.2.0] - 2018-03-26

**Intelligent polling to reduce system load**

* feat: after session is closed stop polling for updates
* style: format error template
* style: format session template

### [2.1.1] - 2018-03-26

**Fixing invalid URLs for services on installations without context path**

* fix: always use correct path for REST requests

### [2.1.0] - 2018-03-26

**Votes with question mark always have to talk**

* refactor: move cards list to enum
* refactor: remove obsolete base class
* feat: question mark always has to talk
* feat: add new logo for Scrum Poker plugin
* fix: correct level for configuration heading

### [2.0.1] - 2018-03-24

**Fixing that no agreement is shown when question marks exist**

* test: add tests for REST endpoint
* fix: agreement should not ignore question marks

### [2.0.0] - 2018-03-24

**Complete rework of the frontend using new template engine and REST services**

* test: add test for ScrumPokerCard
* refactor: move to namespace de.codescape for packages
* refactor: move to namespace de.codescape for maven groupId
* refactor: remove unnecessary throws clauses in actions
* docs: update link to issue creation
* chore: update reference to TravisCI build
* fix: update namespace for web resources
* feat: use Mustache template and REST endpoint for Scrum Poker session
* feat: simplify URLs for starting a Scrum Poker session
* chore: remove deprecated actions in favor of REST endpoints
* style: adjust icon to invite people talking to each other
* feat: improve polling mechanism on Scrum Poker session page

[2.8.0]: https://github.com/codescape/jira-scrum-poker/compare/2.7.0...2.8.0
[2.7.0]: https://github.com/codescape/jira-scrum-poker/compare/2.6.0...2.7.0
[2.6.0]: https://github.com/codescape/jira-scrum-poker/compare/2.5.0...2.6.0
[2.5.0]: https://github.com/codescape/jira-scrum-poker/compare/2.4.1...2.5.0
[2.4.1]: https://github.com/codescape/jira-scrum-poker/compare/2.4.0...2.4.1
[2.4.0]: https://github.com/codescape/jira-scrum-poker/compare/2.3.0...2.4.0
[2.3.0]: https://github.com/codescape/jira-scrum-poker/compare/2.2.0...2.3.0
[2.2.0]: https://github.com/codescape/jira-scrum-poker/compare/2.1.1...2.2.0
[2.1.1]: https://github.com/codescape/jira-scrum-poker/compare/2.1.0...2.1.1
[2.1.0]: https://github.com/codescape/jira-scrum-poker/compare/2.0.1...2.1.0
[2.0.1]: https://github.com/codescape/jira-scrum-poker/compare/2.0.0...2.0.1
[2.0.0]: https://github.com/codescape/jira-scrum-poker/compare/1.12.0...2.0.0
