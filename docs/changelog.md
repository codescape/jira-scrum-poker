---
layout: default
title: Changelog
category: Administration
---

{% include changelog-preface.md %}

* Table of Contents
{:toc}

### [Unreleased]

* test: add Maven profile with maven-dependency-check for Data Center review
* docs: document maven profile for Data Center review

### [22.04.0] - 2022-04-23

**Security release for CVE-2022-22965**

* feat: verify support for latest Jira patch releases 8.22.2, 8.20.8, 8.13.20
* chore: update library versions to version defined in Jira project 8.22.2
* chore: update Spring library to version 5.3.18 (CVE-2022-22965)

### [22.03.0] - 2022-03-30

**Improve loading performance and ensure valid configuration**

* feat: verify support for latest patch releases
* feat: verify support for Jira 8.22.0 and long-term-support releases
* feat: improve configuration loading performance
* feat: verify support for latest Jira patch releases 8.22.1, 8.20.7, 8.13.19
* feat: prevent illegal configuration for session timeout

### [22.01.0] - 2022-01-16

**Explicitly activate and disable Scrum Poker on project-level**

* docs: add developer documentation to workflow function
* feat: verify support for Jira 8.21.0 and long-term-support releases
* feat: verify support for latest patch releases
* feat: add option to explicitly disable Scrum Poker on project-level
* feat: add upgrade task to migrate from old setting to new setting
* docs: update documentation to explain configuration options
* feat: i18n for new configuration options on project configuration
* docs: create archive page for all 21.x releases
* docs: fix table of contents for 21.x releases

### Older versions

Older versions have been moved into separate changelog documents grouped by their major version:

* [Scrum Poker for Jira 21.x](/changelog-21x)
* [Scrum Poker for Jira 20.x](/changelog-20x)
* [Scrum Poker for Jira 4.x](/changelog-4x)
* [Scrum Poker for Jira 3.x](/changelog-3x)
* [Scrum Poker for Jira 2.x](/changelog-2x)
* [Scrum Poker for Jira 1.x](/changelog-1x)

[Unreleased]: https://github.com/codescape/jira-scrum-poker/compare/22.04.0...HEAD
[22.04.0]: https://github.com/codescape/jira-scrum-poker/compare/22.03.0...22.04.0
[22.03.0]: https://github.com/codescape/jira-scrum-poker/compare/22.01.0...22.03.0
[22.01.0]: https://github.com/codescape/jira-scrum-poker/compare/21.11.0...22.01.0
