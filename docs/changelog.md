---
layout: default
title: Changelog
category: Administration
---

{% include changelog-preface.md %}

* Table of Contents
{:toc}

### [Unreleased]

* docs: improve developer documentation for common tasks
* chore: remove configuration for Travis CI (#140)
* feat: verify support for Jira 8.20.0 and latest enterprise patch releases
* feat: verify support for Jira 8.20.1 and latest enterprise patch releases
* test: improve test setup for displayed reference issues
* feat: render issue description with the configured renderer
* docs: add content to frequently asked questions in documentation
* feat: verify support for Jira 8.20.2 and latest enterprise patch releases

### [21.10.0] - 2021-10-06

**Manage Scrum Poker for your projects as a project administrator**

* feat: verify support for Jira 8.19.0 and latest enterprise patch releases
* feat: project administrators can configure Scrum Poker for the given project
* feat: verify support for Jira 8.19.1 and latest enterprise patch releases
* refactor: use non deprecated assertion methods and optimize hamcrest imports

### [21.07.0] - 2021-07-23

**Find active Scrum Poker sessions with standard JQL**

* docs: improve release guidelines
* feat: add support for Jira 8.17.0 and latest enterprise patch releases
* chore: update library versions to version defined in Jira project
* feat: introduce JQL function to query for active Scrum Poker sessions
* docs: fix headings in documentation for joining Scrum Poker sessions
* docs: fix display of long code statements for mobile devices
* docs: improve documentation for JQL function
* feat: add support for Jira 8.18.0 and latest enterprise patch releases
* refactor: cleanup code for millisecond calculation
* chore: drop support for Jira Software versions older than 8.5.x

### [21.05.0] - 2021-05-09

**Improve XSRF security for all configuration pages**

* chore: annual review for Data Center approval
* feat: introduce XSRF protection for health check
* feat: introduce XSRF protection for error log
* feat: introduce XSRF protection for global configuration
* feat: introduce XSRF protection for project configuration

### [21.04.0] - 2021-04-19

**Update compatibility and improve Getting Started page**

* feat: open documentation in new browser window
* feat: add support for Jira 8.16.0 and latest enterprise patch releases

### [21.02.0] - 2021-02-21

**Improve Health Check and Getting Started page**

* feat: reduce usage of colors on getting started page
* feat: add support for Jira 8.15.0
* feat: add health check for supported estimate field type (#112)
* feat: add support for latest enterprise patch releases
* test: improve tests for correctness of message bundles

### [21.01.1] - 2021-01-30

**Introduce Getting Started page to assist with the first steps**

* feat: add support for Jira 8.14.1 and latest enterprise patch releases
* feat: first version of the getting started page and better exception handling (#123)
* test: add tests for getting started page (#123)
* feat: add fallback colors for getting started page (#123)
* feat: include references to documentation and service desk (#123)
* docs: add documentation url to Maven POM
* feat: add link to review app or request features to getting started page (#137)
* feat: use Scrum Poker for Jira specific colors on getting started page

### [21.01.0] - 2021-01-08

**Support more estimate field types and fix global activation**

* docs: add frequently asked questions to online documentation
* feat: support estimate fields that are select lists or radio buttons (#120)
* refactor: improve code to persist estimates depending on field type
* fix: persist unchecked value of activate Scrum Poker in global configuration

### Older versions

Older versions have been moved into separate changelog documents grouped by their major version:

* [Scrum Poker for Jira 20.x](/changelog-20x)
* [Scrum Poker for Jira 4.x](/changelog-4x)
* [Scrum Poker for Jira 3.x](/changelog-3x)
* [Scrum Poker for Jira 2.x](/changelog-2x)
* [Scrum Poker for Jira 1.x](/changelog-1x)

[Unreleased]: https://github.com/codescape/jira-scrum-poker/compare/21.10.0...HEAD
[21.10.0]: https://github.com/codescape/jira-scrum-poker/compare/21.07.0...21.10.0
[21.07.0]: https://github.com/codescape/jira-scrum-poker/compare/21.05.0...21.07.0
[21.05.0]: https://github.com/codescape/jira-scrum-poker/compare/21.04.0...21.05.0
[21.04.0]: https://github.com/codescape/jira-scrum-poker/compare/21.02.0...21.04.0
[21.02.0]: https://github.com/codescape/jira-scrum-poker/compare/21.01.1...21.02.0
[21.01.1]: https://github.com/codescape/jira-scrum-poker/compare/21.01.0...21.01.1
[21.01.0]: https://github.com/codescape/jira-scrum-poker/compare/20.12.1...21.01.0
