---
layout: default
title: Changelog 4.x
---

{% include changelog-preface.md %}

* Table of Contents
{:toc}

### [4.10.0] - 2020-03-28

**Configure your card set and benefit from the improved configuration page**

* chore: implement against Jira 8.8.0
* test: test against Jira 7.13.13 and 8.5.4 of previous enterprise releases
* feat: one globally used card set can be configured via database (#18)
* docs: update compatibility matrix for Jira 8.8
* chore: adapt dependency versions according to Jira 8.8.0 (#95)
* test: suppress unchecked cast warnings for class matcher
* feat: default card set can be configured and is persisted in session (#18)
* feat: add translations for the configuration of the default card set (#18)
* fix: calculate bounded votes based on cards for Scrum Poker session (#18)
* docs: document the configuration of the default card set (#18)
* refactor: better naming for card set service
* fix: correct escaping for special characters in German translation  
* feat: allow to switch back to default settings in global configuration (#100)
* fix: do not save empty values instead of null if nothing is selected
* feat: require mandatory fields in global configuration
* fix: ignore required fields when switching back to default settings
* refactor: simplify way to identify pressed button for chosen action
* refactor: static import card constants for readability
* refactor: simplify and shorten class names for services
* test: add tests for error active object

### [4.9.0] - 2020-02-16

**Minor improvements and updates to compatibility with Jira 8.7**

* feat: current page name is included in page title on active session page
* chore: implement against Jira patch version 8.7.1
* fix: allow to save configuration after selection to display all comments
* fix: ensure migration task 7 performs correctly
* chore: format style sheets according to standard code style
* refactor: inject and import Active Objects in preferred way
* test: test against latest patch versions of previous enterprise releases
* chore: drop support for Jira Software versions older than 7.13.x
* docs: update address in imprint for online documentation

### [4.8.2] - 2020-01-22

**Bugfix for configuration option and compatibility with latest Jira version**

* docs: update compatibility matrix
* refactor: improve descriptiveness of private constructor of utility class
* docs: add documentation to Scrum Poker card class
* docs: ignore temporary jekyll directory for generated online documentation
* docs: add hamburger menu to mobile version of online documentation
* chore: implement against Jira patch version 8.6.1
* fix: label for configuration option dropdown on board is clickable

### [4.8.1] - 2019-12-24

**Bugfixes for compatibility with Oracle dialect and configuration option**

* fix: allow to restrict revealing Scrum Poker deck to the session creator
* chore: cleanup formatting of changelog
* fix: lower case query aliases cause SQL error for Oracle drivers

### [4.8.0] - 2019-12-18

**Improved session handling with better timeout management**

* feat: improve session timeouts with new last update date (#74)
* refactor: cleanup for upgrade task for last update date
* refactor: improve code to calculate and deal with session timeouts
* docs: rework compatibility matrix and recommend updating to the latest version
* docs: include Jira Data Center in compatibility matrix
* chore: use latest version of AMPS plugin (#89)
* refactor: rename condition for visibility of board dropdown
* chore: implement against Jira patch version 8.6.0
* chore: update tested enterprise version to Jira patch version 8.5.2
* chore: update tested oldest version to Jira patch version 7.6.17

### [4.7.0] - 2019-11-20

**Modernize logos and banners for Scrum Poker for Jira**

* chore: cleanup unused imports on upgrade class
* chore: implement against Jira patch version 8.5.1
* chore: remove dependency to unused library velocity-htmlsafe
* chore: use latest version of hamcrest for testing
* docs: improve app description for Atlassian Marketplace
* feat: improved logo for Marketplace listing and active sessions page
* feat: use improved logo as favicon for documentation
* docs: add documentation page with supported languages
* feat: provide a marketing banner for Marketplace listing
* docs: remove old versions from compatibility matrix

### [4.6.0] - 2019-10-31

**Better estimates with comments on Scrum Poker session page**

* docs: add version headings for older releases of Scrum Poker
* test: use Mockito version 3.1.0 for mocks in tests
* chore: implement against Jira patch version 8.5.0
* docs: update compatibility matrix
* feat: display comments for issue on Scrum Poker session page
* feat: add configuration option to display comments on Scrum Poker session page
* docs: create online documentation for configuration options
* test: fix test for default option during upgrade task

### [4.5.0] - 2019-10-11

**Improved compatibility for Oracle database and latest Jira versions**

* refactor: restructure folders for Scrum Poker logo
* chore: implement against Jira patch version 8.4.0 (#79)
* docs: update compatibility matrix
* docs: improve installation instructions with license step
* docs: improve imprint and add legal information
* docs: better explanation to join a Scrum Poker session
* chore: implement against Jira patch version 8.4.2 (#81)
* chore: implement against Jira enterprise patch versions 7.13.8 and 7.6.16
* fix: rename aliases to not use reserved words in query (#82)
* docs: improve documentation for error log check in health check
* test: improve test cases for reference stories

### [4.4.0] - 2019-09-05

**Improved styling and wording for all administration pages**

* docs: improve wording on error log documentation page
* chore: remove obsolete changelog in root directory
* feat: introduce namespace for cascading style sheets
* style: add braces also around simple conditions
* feat: add translation for the health check feature in French and German
* feat: improve presentation for no errors on error log page
* feat: improve styling for error message and active sessions link on error page
* feat: improve styling of back button on error page
* feat: improve styling of save buttons on global and project configuration page
* feat: improve styling of start button on health check page
* feat: correct numbering of errors in error log

### [4.3.0] - 2019-08-28

**Error Log for problem analysis by Jira administrators**

* chore: implement against Jira patch version 8.3.3
* docs: add favicon for online documentation
* docs: improve documentation for participation on Scrum Poker sessions
* docs: improve invite to contact vendor
* docs: add imprint to online documentation
* docs: add link to imprint in footer
* docs: move imprint into legal section
* docs: add icons to introduction page
* refactor: introduce service interface for the estimation field service
* feat: add error logging capabilities to all possible error locations
* docs: add documentation to ScrumPokerConstants class
* feat: return errors in correct order and add test coverage
* refactor: explicitly autowire constructor arguments
* fix: rename error timestamp column to avoid using reserved database keywords
* refactor: use parseX methods instead of valueOf methods for Boolean and Integer
* feat: improve error messages for failures when saving estimations
* feat: add error log page to the plugin administration to display errors
* feat: provide better description on the health check page
* feat: add translation for error log page
* test: implement test coverage for error log action
* docs: add online documentation for error log page

### [4.2.0] - 2019-08-03

**Configurable permission check to confirm estimations**

* docs: include supported languages on support page in online documentation
* chore: implement against Jira patch version 8.3.0
* docs: move support page to a more obvious menu item
* chore: fix typo in constant in upgrade class
* feat: check permission of current user to save estimate (#68)
* feat: add documentation for permission check configuration (#68)
* feat: improve structure of configuration screen
* feat: add upgrade task with default value for permission check (#68)
* feat: buttons to confirm estimation only displayed if permitted (#68)
* docs: update online documentation for Scrum Poker configuration (#75)

### [4.1.0] - 2019-07-16

**Health Check for your Scrum Poker for Jira installation**

* docs: add release headlines to changelog
* refactor: improve license and issue check when starting a session
* chore: switch to Xenial distribution for Travis CI build
* chore: fix the spelling of Scrum Poker in comments and changes
* test: remove todo for deployment with licensing disabled after successful test
* feat: health check for Scrum Poker app available in configuration (#67)
* feat: add check for validity of license to health check (#67)
* test: implement test coverage for health check logic (#67)
* docs: notice that a GitHub account is required to create new tickets
* docs: improve support page in online documentation
* docs: add online documentation for the health check feature
* feat: add check for Scrum Poker enabled globally or explicitly (#67)
* feat: add German translation for health check (#67)
* feat: add French translation for health check (#67)
* chore: implement against Jira patch versions 8.2.3, 7.13.6 and 7.6.14 (#70)

### [4.0.1] - 2019-06-16

**Certified support and compatibility for Jira Data Center**

* docs: update compatibility matrix
* docs: shorten initial list of table of contents
* test: update versions of testing libraries
* refactor: change order of methods to comply with other upgrades
* refactor: optimize imports and remove unused imports
* refactor: move configuration templates into separate directory
* chore: implement against latest Jira patch version 8.2.2
* docs: add support page to online documentation
* feat: add license check for installed valid license during session creation

### [4.0.0] - 2019-06-02

**Certified support and compatibility for Jira Data Center**

*This version was not published on Atlassian Marketplace because of a pending approval for Jira Data Center.*

* chore: update tested patch version of Jira 7.6 and 7.13 enterprise release
* refactor: optimize imports and remove unused imports
* fix: correct escaping for German umlauts in resource bundle
* docs: add information badges to readme
* chore: change configuration to produce licensed version by default
* feat: shorten description to start a new session on active session list
* refactor: introduce global settings object to load and persist settings (#65)
* feat: add configuration option to show and hide Scrum Poker dropdown on boards
* docs: update documentation for the new configuration option
* docs: document global settings options in class
* docs: move compatibility matrix into online documentation
* docs: improve compatibility matrix and explain development and testing
* refactor: extract logic to build a reference story
* chore: implement against latest Jira patch version 8.2.1
* test: improve test to ensure correct resource bundles for internationalization
* refactor: move internationalization files into separate directory

[4.10.0]: https://github.com/codescape/jira-scrum-poker/compare/4.9.0...4.10.0
[4.9.0]: https://github.com/codescape/jira-scrum-poker/compare/4.8.2...4.9.0
[4.8.2]: https://github.com/codescape/jira-scrum-poker/compare/4.8.1...4.8.2
[4.8.1]: https://github.com/codescape/jira-scrum-poker/compare/4.8.0...4.8.1
[4.8.0]: https://github.com/codescape/jira-scrum-poker/compare/4.7.0...4.8.0
[4.7.0]: https://github.com/codescape/jira-scrum-poker/compare/4.6.0...4.7.0
[4.6.0]: https://github.com/codescape/jira-scrum-poker/compare/4.5.0...4.6.0
[4.5.0]: https://github.com/codescape/jira-scrum-poker/compare/4.4.0...4.5.0
[4.4.0]: https://github.com/codescape/jira-scrum-poker/compare/4.3.0...4.4.0
[4.3.0]: https://github.com/codescape/jira-scrum-poker/compare/4.2.0...4.3.0
[4.2.0]: https://github.com/codescape/jira-scrum-poker/compare/4.1.0...4.2.0
[4.1.0]: https://github.com/codescape/jira-scrum-poker/compare/4.0.1...4.1.0
[4.0.1]: https://github.com/codescape/jira-scrum-poker/compare/4.0.0...4.0.1
[4.0.0]: https://github.com/codescape/jira-scrum-poker/compare/3.13.0...4.0.0
