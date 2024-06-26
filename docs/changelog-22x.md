---
layout: default
title: Changelog 22.x
category: Administration
---

{% include changelog-preface.md %}

* Table of Contents
{:toc}

### [22.11.0] - 2022-11-21

**Compatibility updates for latest Jira versions**

* feat: ensure compatibility with Jira 9.3.0, 8.20.13 and 8.13.26
* chore: drop support for 8.13.x and ensure compatibility for latest versions
* chore: update library versions to version defined in Jira project 9.4.0

### [22.08.0] - 2022-08-28

**Bugfix for saving empty project specific settings**

* docs: improve documentation and remove typos
* chore: optimize imports and code style
* test: use GitHub actions for continuous integration
* feat: ensure compatibility with Jira 9.1.0 and 8.20.11
* fix: project settings cannot be saved without any settings
* feat: ensure compatibility with Jira 9.2.0, 8.20.12 and 8.13.25

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

[22.11.0]: https://github.com/codescape/jira-scrum-poker/compare/22.08.0...22.11.0
[22.08.0]: https://github.com/codescape/jira-scrum-poker/compare/22.07.0...22.08.0
[22.07.0]: https://github.com/codescape/jira-scrum-poker/compare/22.06.1...22.07.0
[22.06.1]: https://github.com/codescape/jira-scrum-poker/compare/22.06.0...22.06.1
[22.06.0]: https://github.com/codescape/jira-scrum-poker/compare/22.05.0...22.06.0
[22.05.0]: https://github.com/codescape/jira-scrum-poker/compare/22.04.0...22.05.0
[22.04.0]: https://github.com/codescape/jira-scrum-poker/compare/22.03.0...22.04.0
[22.03.0]: https://github.com/codescape/jira-scrum-poker/compare/22.01.0...22.03.0
[22.01.0]: https://github.com/codescape/jira-scrum-poker/compare/21.11.0...22.01.0
