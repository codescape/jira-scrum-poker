---
layout: default
title: Changelog
category: Administration
---

{% include changelog-preface.md %}

* Table of Contents
{:toc}

### [Unreleased]

* docs: improve release guidelines

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

[Unreleased]: https://github.com/codescape/jira-scrum-poker/compare/21.05.0...HEAD
[21.05.0]: https://github.com/codescape/jira-scrum-poker/compare/21.04.0...21.05.0
[21.04.0]: https://github.com/codescape/jira-scrum-poker/compare/21.02.0...21.04.0
[21.02.0]: https://github.com/codescape/jira-scrum-poker/compare/21.01.1...21.02.0
[21.01.1]: https://github.com/codescape/jira-scrum-poker/compare/21.01.0...21.01.1
[21.01.0]: https://github.com/codescape/jira-scrum-poker/compare/20.12.1...21.01.0
