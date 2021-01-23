---
layout: default
title: Changelog
category: Administration
---

{% include changelog-preface.md %}

* Table of Contents
{:toc}

### [Unreleased]

* feat: add support for Jira 8.14.1 and latest enterprise patch releases
* feat: first version of the getting started page and better exception handling
* test: add tests for getting started page

### [21.01.0] - 2021-01-08

**Support more estimate field types and fix global activation**

* docs: add frequently asked questions to online documentation
* feat: support estimate fields that are select lists or radio buttons (#120)
* refactor: improve code to persist estimate depending on field type
* fix: persist unchecked value of activate Scrum Poker in global configuration

### Older versions

Older versions have been moved into separate changelog documents grouped by their major version:

* [Scrum Poker for Jira 20.x](/changelog-20x)
* [Scrum Poker for Jira 4.x](/changelog-4x)
* [Scrum Poker for Jira 3.x](/changelog-3x)
* [Scrum Poker for Jira 2.x](/changelog-2x)
* [Scrum Poker for Jira 1.x](/changelog-1x)

[Unreleased]: https://github.com/codescape/jira-scrum-poker/compare/21.01.0...HEAD
[21.01.0]: https://github.com/codescape/jira-scrum-poker/compare/20.12.1...21.01.0
