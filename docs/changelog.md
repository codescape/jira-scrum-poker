---
layout: default
title: Changelog
category: Administration
---

{% include changelog-preface.md %}

* Table of Contents
{:toc}

### [Unreleased]

* feat: ensure compatibility with Jira 9.17.2, 9.12.12, 9.4.25
* chore: add Docker configuration for Jira 10
* chore: prepare for Jira 10
* chore: move from @Autowired to @Inject for all dependency injections
* chore: move to new Jackson dependencies for REST services and mapping
* chore: migrate to REST 2 for all REST endpoints
* chore: remove @Component from all Condition classes and cleanup OSGi imports
* chore: depend on Jira platform BOMs for versioning
* chore: remove parallel configuration for different Jira versions
* chore: add workaround for not working QuickReload in Docker run
* chore: add allow-list for methods called from Velocity templates
* chore: update .gitignore for Docker
* docs: improve documentation for Docker usage
* fix: improve Velocity template allow-list for comments
* fix: improve Velocity template allow-list and implementation for project settings
* chore: compile against Java 17 since this is recommended for Jira 10
* fix: improve Velocity template allow-list for active session list
* feat: add support for Jira 10 dark mode and light mode

### [24.08.0] - 2024-08-21

**Compatibility updates for Jira 9.14 - 9.17**

* feat: ensure compatibility with Jira 9.14.0, 9.12.5, 9.4.18
* chore: add results for Data Center re-approval
* feat: ensure compatibility with Jira 9.15.0
* feat: ensure compatibility with Jira 9.16.0, 9.12.9, 9.4.22
* feat: ensure compatibility with Jira 9.17.0, 9.4.24
* feat: ensure compatibility with Jira 9.17.1, 9.12.11

### [24.01.0] - 2024-01-27

**Compatibility updates for Jira 9.13**

* feat: ensure compatibility with Jira 9.12.0
* feat: ensure compatibility with Jira 9.12.1 and 9.4.14
* test: add test coverage for error log action
* test: add test coverage for health check action
* test: add test coverage for configuration action
* docs: move changelog for 23.x releases into archive
* chore: use jira-project to define all library versions
* feat: ensure compatibility with Jira 9.13.0, 9.12.2 and 9.4.15

### Older versions

Older versions have been moved into separate changelog documents grouped by their major version:

* [Scrum Poker for Jira 23.x](/changelog-23x)
* [Scrum Poker for Jira 22.x](/changelog-22x)
* [Scrum Poker for Jira 21.x](/changelog-21x)
* [Scrum Poker for Jira 20.x](/changelog-20x)
* [Scrum Poker for Jira 4.x](/changelog-4x)
* [Scrum Poker for Jira 3.x](/changelog-3x)
* [Scrum Poker for Jira 2.x](/changelog-2x)
* [Scrum Poker for Jira 1.x](/changelog-1x)

[Unreleased]: https://github.com/codescape/jira-scrum-poker/compare/24.08.0...HEAD
[24.08.0]: https://github.com/codescape/jira-scrum-poker/compare/24.01.0...24.08.0
[24.01.0]: https://github.com/codescape/jira-scrum-poker/compare/23.11.0...24.01.0
