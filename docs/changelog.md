---
layout: default
title: Changelog
category: Administration
---

{% include changelog-preface.md %}

* Table of Contents
{:toc}

### [Unreleased]

* docs: improve documentation and remove typos
* chore: optimize imports and code style
* test: use GutHub actions for continuous integration

### [22.07.0] - 2022-07-11

**Universal App with compatibility for Jira 8 and Jira 9**

* docs: update compatibility matrix
* feat: ensure compatibility with Jira 9.0.0, Jira 8.13.x and Jira 8.20.x
* chore: drop support for Jira 8.5.x

### [22.06.1] - 2022-06-26

**Scrum Poker is ready for Jira 9**

* fix: use data transfer object instead of active object for project settings

### [22.06.0] - not released

**Scrum Poker is ready for Jira 9**

* fix: use correct link on sub-tasks list for current assignee
* feat: ensure support for latest Jira releases 9.0.0, 8.20.9, 8.13.1

### [22.05.0] - 2022-05-08

**Navigation improvements and Data Center review**

* test: add Maven profile with maven-dependency-check for Data Center review
* docs: document Maven profile for Data Center review
* chore: reorganize results for annual Data Center reviews
* feat: allow jumping back to where you started Scrum Poker from (#53)

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

[Unreleased]: https://github.com/codescape/jira-scrum-poker/compare/22.07.0...HEAD
[22.07.0]: https://github.com/codescape/jira-scrum-poker/compare/22.06.1...22.07.0
[22.06.1]: https://github.com/codescape/jira-scrum-poker/compare/22.06.0...22.06.1
[22.06.0]: https://github.com/codescape/jira-scrum-poker/compare/22.05.0...22.06.0
[22.05.0]: https://github.com/codescape/jira-scrum-poker/compare/22.04.0...22.05.0
[22.04.0]: https://github.com/codescape/jira-scrum-poker/compare/22.03.0...22.04.0
[22.03.0]: https://github.com/codescape/jira-scrum-poker/compare/22.01.0...22.03.0
[22.01.0]: https://github.com/codescape/jira-scrum-poker/compare/21.11.0...22.01.0
