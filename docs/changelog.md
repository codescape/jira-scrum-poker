---
layout: default
title: Changelog
category: Administration
---

{% include changelog-preface.md %}

* Table of Contents
{:toc}

### [Unreleased]

* docs: ensure consistent naming for estimate field
* feat: add workflow function to start Scrum Poker session (#104)
* feat: add support for Jira 8.10.0 and test latest enterprise versions

### [20.06.1] - 2020-06-19

**Ensure Scrum Poker compatibility with other installed plugins**

* test: remove unused mock hints from test case and simplify test
* docs: add developer documentation regarding unique simple name for actions (#115)
* refactor: add unique prefix to active sessions action (#115)
* refactor: add unique prefix to global configuration action (#115)
* refactor: add unique prefix to project configuration action (#115)
* refactor: add unique prefix to health check action (#115)
* refactor: add unique prefix to error log action (#115)
* docs: remove highlights from readme in favor of feature list
* feat: add support for Jira 8.9.1

### [20.06.0] - 2020-06-13

**Different buttons to start and join a Scrum Poker session**

* chore: update dependency versions from Jira 8.9.0 project
* docs: improve installation instructions in online documentation
* test: add test coverage for valid Scrum Poker session URL links
* feat: show option to join Scrum Poker session if already started (#109)
* test: perform required Lucene index timing tests for Data Center approval
* docs: mark external links in documentation and improve overall visual appearance
* docs: add feature overview to online documentation (#98)
* docs: add features to feature overview in online documentation (#98)

### [20.05.3] - 2020-05-20

**Improved configuration for selective activation of Scrum Poker**

* docs: document required admin role to configure Scrum Poker for Jira (#116)
* refactor: rename variables in health check to better explain checks
* docs: add documentation to logic for need to talk flag
* style: clean up formatting of plugin configuration
* feat: rename global setting for Scrum Poker activation (#105)
* feat: rename and improve project setting for Scrum Poker activation (#105)
* chore: optimize imports for test classes
* feat: add support for Jira 8.9.0
* docs: improve wording in changelog

### [20.05.2] - 2020-05-14

**Configuration available for all users who can install Marketplace apps**

* docs: add summary for older versions currently missing one
* feat: require admin and not sysadmin role to configure Scrum Poker for Jira

### [20.05.1] - 2020-05-10

**Improvements for non-numeric card sets and active sessions overview**

* test: improve mock usage in tests for session entity mapper
* docs: fix reference to version comparison in changelog
* docs: correct version number in deprecation information
* feat: catch and report incompatible estimate values for custom field when saving (#114)
* docs: update configuration documentation to recent changes
* feat: display own estimates of current user on active sessions page (#106)
* docs: document supported field types for the estimate field
* docs: separate changelog documents accumulated by major version

### [20.05.0] - 2020-05-09

**Configure and use card sets with non-numeric and floating point cards**

* docs: adapt version 2.0 of the Contributor Covenant Code of Conduct
* docs: update readme and reduce to most relevant information
* docs: rename license and update link from readme
* docs: improve contributing page and separate credits
* refactor: rename actions for project specific and global configuration
* refactor: rename actions for active sessions, error log and current session
* refactor: rename and shorten service for card sets
* feat: add health check to find problems with configured card set
* refactor: centralize identification for assignable votes
* fix: correct value for coffee card in German translation for field description
* docs: improve styling for main navigation in online documentation
* docs: update compatibility matrix with database hints
* docs: improve internal developer documentation
* docs: add developer documentation for several elements in POM
* refactor: introduce the concept of a card to allow further logic
* refactor: optimize imports
* refactor: improve readability of mapper for REST representation of a session
* refactor: rename estimate service in preparation for new custom field logic
* refactor: rename story point field in preparation for new custom field logic
* feat: calculation of bounded votes supports non-numeric card values (#102)
* feat: simplify logic to calculate agreement reached flag (#102)
* chore: implement against Jira 8.8.1
* refactor: cleanup code for composing dates to display values
* refactor: improve speed for calculation for revealing deck permission
* refactor: improve readability for session mapping
* feat: support non-numeric estimates (#102)
* feat: treat all votes that are not a special card as assignable (#102)
* feat: evaluate need to talk flag for all kind of cards (#102)
* feat: persist estimate based on custom field type (#102)
* feat: only show supported custom fields in configuration (#102)
* refactor: optimize loading card set for mapped session (#102)
* feat: show parent task information for sub tasks (#110)
* feat: improve translation for estimate field configuration

### Older versions

Older versions have been moved into separate changelog documents grouped by their major version:

* [Scrum Poker for Jira 4.x](/changelog-4x)
* [Scrum Poker for Jira 3.x](/changelog-3x)
* [Scrum Poker for Jira 2.x](/changelog-2x)
* [Scrum Poker for Jira 1.x](/changelog-1x)

[Unreleased]: https://github.com/codescape/jira-scrum-poker/compare/20.06.1...HEAD
[20.06.1]: https://github.com/codescape/jira-scrum-poker/compare/20.06.0...20.06.1
[20.06.0]: https://github.com/codescape/jira-scrum-poker/compare/20.05.3...20.06.0
[20.05.3]: https://github.com/codescape/jira-scrum-poker/compare/20.05.2...20.05.3
[20.05.2]: https://github.com/codescape/jira-scrum-poker/compare/20.05.1...20.05.2
[20.05.1]: https://github.com/codescape/jira-scrum-poker/compare/20.05.0...20.05.1
[20.05.0]: https://github.com/codescape/jira-scrum-poker/compare/4.10.0...20.05.0
