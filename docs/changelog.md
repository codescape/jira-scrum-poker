---
layout: default
title: Changelog
---

This changelog helps developers and users to keep track of new features, fixes and improvements for Scrum Poker for Jira.
Click on the version in the following list to see all changes since the previous version.

* Table of Contents
{:toc}

### [Unreleased]

* feat: current page name is included in page title on active session page
* chore: implement against Jira patch version 8.7.0
* fix: allow to save configuration after selection to display all comments
* fix: ensure migration task 7 performs correctly
* chore: format style sheets according to standard code style
* refactor: inject and import Active Objects in preferred way

### [4.8.2] - 2020-01-22

**Bugfix for configuration option and compatibility for latest Jira version**

* docs: update compatibility matrix
* refactor: improve descriptiveness of private constructor of utility class
* docs: add documentation to Scrum Poker card class
* docs: ignore temporary jekyll directory for generated online documentation
* docs: add hamburger menu to mobile version of online documentation
* chore: implement against Jira patch version 8.6.1
* fix: label for configuration option dropdown on board is clickable
* docs: update address in imprint for online documentation

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

### [3.13.0] - 2019-04-22

**Certified support and compatibility for Jira Data Center**

*This version was not published on Atlassian Marketplace because of a pending approval for Jira Data Center.*

* docs: change order of highlights in readme
* feat: migrate old votes with question mark to new string format
* test: improve test coverage and suppress unused warnings
* test: include performance test suite for Jira Data Center
* test: add markers to session list and detail view to identify pages
* feat: copy session link with a button on the share session screen (#62)
* feat: document Jira Data Center compatibility in plugin descriptor
* style: format css code consistently
* feat: prevent images in issue description from floating into poker elements
* feat: display dates in user specific date format (#57)
* feat: explain how to start new sessions on empty active session list

### [3.12.0] - 2019-04-05

**Improved styling for break and question cards and compatibility with Jira 8.1.x**

* feat: signal that a break is needed only when deck is revealed
* docs: describe possible usage of test groups
* feat: use Font Awesome to display icons for coffee card and question mark
* chore: implement against latest Jira patch version 8.1.0
* chore: update tested patch version of Jira 7.6 enterprise release
* docs: improve compatibility matrix in readme

### [3.11.1] - 2019-04-01

**Compatibility fix for Jira 7.x in Scrum Poker 3.11.0**

* docs: move changelog into online documentation
* docs: improve changelog with additional description
* fix: do not rely on commons-lang 3.5 for checking numeric values
* test: add test groups for supported versions of Jira (#55)

### [3.11.0] - 2019-03-30

**Introducing coffee card for breaks and improved estimation confirmation screen**

* chore: use short name Scrum Poker inside Jira GUI
* style: format configuration files
* docs: update docs according to usage of the short name
* refactor: remove usage of deprecated API and switch to new method
* fix: IntelliJ inspection results
* docs: add section for Maven dependency analysis
* chore: cleanup dependencies and remove unnecessary dependencies
* chore: explicitly reference required dependencies
* chore: re-establish quick reload functionality
* feat: update german translation to fit to Jira internal wording
* chore: non verbose logging on maven lifecycle
* test: remove unused mocks from test and clean up test logging
* docs: use long name in title of online documentation
* docs: add documentation to all action classes
* chore: implement against latest Jira patch version 8.0.2
* feat: persist and show user and date when confirming an estimation (#56)
* docs: improve headings and navigation in online documentation
* feat: introduce a coffee card to signal that a participant needs a break (#13)

### [3.10.0] - 2019-02-14

**Scrum Poker for Jira now supports Jira 8**

* chore: implement against latest Jira version
* test: improve tests for validity of message bundles for internationalization
* feat: implement with compatibility for Jira 8.0.0
* chore: update pom documentation and remove not required import packages
* chore: use Scrum Poker for Jira as new name and refer to it as a Jira app
* docs: update compatibility matrix

### [3.9.0] - 2019-02-01

**Improved button styling, new configuration options and French translation**

* feat: improve button styling to refresh session list
* feat: improve button styling for Scrum Poker session page
* chore: update dependency versions of all used libraries
* feat: show number of votes during Scrum Poker session
* feat: add global configuration to define who is allowed to reveal the deck
* fix: display number of votes correctly for more than one participant
* feat: vertically align buttons with text on buttons
* feat: add complete internationalization of the plugin in French

### [3.8.0] - 2019-01-14

**Configure Scrum Poker per project and share Scrum Poker sessions easily**

* docs: provide EULA and privacy statement in online documentation
* feat: add global configuration to enable or disable Scrum Poker per default
* feat: add per project configuration to enable Scrum Poker individually
* chore: upgrade spring and lucene dependencies to latest stable version
* docs: add online documentation for project specific configuration
* refactor: improve readability and structure of all action classes
* refactor: rename and move transformer classes into separate mapper package
* chore: update URL for Atlassian Maven repository to new non deprecated URL
* refactor: correct spelling for timeout in interface an implementation
* refactor: build Atlassian plugin coordinates in Maven POM and use in plugin
* refactor: reorder web-item parameters in configuration
* feat: add share option to scan QR code or copy link to Scrum Poker session
* refactor: cleanup of Javascript code for QR code and copy link initialization
* docs: add contact mail on start page of online documentation
* docs: add call to action button to download and install Scrum Poker
* docs: document process of sharing a Scrum Poker session via URL and QR code

### [3.7.0] - 2018-12-03

**Compatibility with Jira 7.13**

* test: cleanup test for REST endpoint and remove unnecessary test data
* test: cleanup test for automatic upgrade task and remove unnecessary test data
* test: improve naming of test methods to better reflect test case
* feat: improve error page and error massage in case of an error
* feat: automatically install Jira Software in development mode
* refactor: retrieve current user in separate and reusable method
* chore: implement against latest version of Atlassian SDK
* test: add test for correct behaviour when cancelling a session
* chore: implement against latest Jira patch version 7.13.0

### [3.6.0] - 2018-09-26

**New styling for session page and badges with estimation distribution count**

* chore: upgrade all dependencies to latest stable version
* feat: adapt Jira style guide for Scrum Poker card layout
* refactor: use Spring annotations and conform to atlassian-spring-scanner
* docs: update inline documentation in POM file
* test: add tests for correct format of message bundles for language support
* docs: add Java documentation for developers to all Active Objects
* test: add tests for automatic migration of old configuration
* refactor: reduce class visibility and add final to constructor assigned fields
* feat: add logging for automatic plugin updates
* feat: use transactions for writing Scrum Poker data (#32)
* feat: show badge with number of votes for every card when cards are revealed
* refactor: format session detail view page
* docs: update inline documentation for style sheets
* docs: add screenshots for online documentation
* docs: add online documentation for the Scrum Poker process
* docs: simplify readme and remove unused screenshots
* refactor: optimize imports and format code and configuration

### [3.5.0] - 2018-09-16

**Readiness for Data Center with user interface improvements**

* docs: improve online documentation layout for mobile devices
* feat: make Scrum Poker accessible from Jira Software boards menu
* docs: update documentation for joining an active session
* refactor: use links starting with uppercase for Scrum Poker
* feat: move Scrum Poker configuration into database (#31)
* test: simplify test and remove unnecessary test data
* feat: add parameter to signal Atlassian Data Center compatibility (#24)
* feat: automatically migrate old configuration into new one and cleanup
* feat: layout improvements for Scrum Poker session to better guide users

### [3.4.0] - 2018-09-15

**Improved Scrum Poker session page and new online documentation**

* fix: correct escape for umlauts in configuration description
* chore: implement against latest Jira version
* refactor: rename Scrum Poker session service implementation
* feat: prevent issue description from floating into poker elements
* refactor: change primary key for persisted votes from int to Long
* feat: do not create Scrum Poker sessions for issues missing estimation field
* test: use Mockito ArgumentMatcher instead of deprecated Mockito Matcher
* test: improve tests and remove unnecessary details in setup
* chore: implement against latest Jira patch version 7.12.1
* feat: sort buttons on Scrum Poker session page by usage frequency
* docs: implement a first version of an online documentation

### [3.3.0] - 2018-09-01

**Improved process of saving estimations to issues in backend and GUI**

* feat: ensure minimum value of zero for session timeout configuration
* feat: show warning/success message when saving estimation to issue
* docs: add inline documentation to CSS code
* feat: improve error message when saving estimation to issue
* test: improve log output for better readability

### [3.2.0] - 2018-08-10

**Scrum Poker sessions can be cancelled by the session starter**

* feat: Scrum Poker session timeout configurable in plugin settings (#19)
* feat: allow the creator of a session to cancel the session (#20)
* chore: implement against latest Jira patch version 7.11.2
* fix: button "Start Scrum Poker" is shown twice (#28)
* feat: improve readability of global plugin configuration
* refactor: use Java 8 lambdas and use null save operations where appropriate

### [3.1.2] - 2018-07-27

**Improvements to reference issues and active sessions list**

* docs: improve privacy statement with details to data usage
* fix: cancel fading out reference issues when references shall be displayed
* fix: hide references immediately when estimation is confirmed
* fix: do not show references where issue does not exist anymore
* chore: implement against latest Jira patch version 7.11.1
* feat: simplify add-on global configuration page

### [3.1.1] - 2018-07-18

**Fixes for robustness of active session list**

* fix: do not fail to display session list for not existing issues
* fix: do not create sessions for not existing issues
* feat: reduce requests to reference issues and fade out slowly in gui
* refactor: improve code for persistence service

### [3.1.0] - 2018-07-17

**Show reference issues during session to help find the estimation**

* feat: show reference issues during session to help find the estimation
* refactor: improve code to transform database model into the view model
* refactor: rename estimation field service and use dependency injection
* refactor: rename settings service and use dependency injection

### [3.0.1] - 2018-07-16

**Fixes for correct visualization of Scrum Poker sessions**

* chore: implement against Jira version 7.11.x
* fix: do announce agreement only for revealed sessions
* fix: hide the revealed session when new votes are added (#23)

### [3.0.0] - 2018-07-13

**Complete rewrite of the persistence layer to persist Scrum Poker sessions**

* chore: implement against latest Jira version 7.10.2
* test: add tests for Scrum Poker configuration action
* refactor: rename and move condition to display Scrum Poker for issue
* refactor: use better base class for condition to render Scrum Poker button
* feat: add link to marketplace on plugin configuration page
* refactor: introducing persistence to Scrum Poker session with Active Objects

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

### [1.12.0] - 2018-03-11

**New Scrum Poker sessions only for estimable items and much cleaner session overview**

* refactor: implement choose card action with testability in mind
* feat: non editable issues do not display Scrum Poker button
* docs: rename JIRA to Jira in all places in the code
* chore: rename plugin to Scrum Poker and add better description
* chore: implement with latest amps and Jira version
* refactor: Java 8 way to create and return session
* style: restructure Maven POM
* feat: remove timed out sessions also in session overview
* feat: improve layout and wording for session view
* refactor: use Integer for estimations everywhere
* chore: update version of amps to 6.3.13

### [1.11.0] - 2018-02-27

**Permission check so that no issues are shown to unprivileged users**

* feat: stop users from opening issues in Scrum Poker they may not see
* feat: add robustness when a key cannot be resolved to a user
* feat: improve error page which will now also appear when user is not allowed to see issue
* refactor: simplify refreshing session data and remove unused code
* docs: fix typo and remove trailing whitespace
* style: add initial version of EditorConfig file
* style: add indent and charset to EditorConfig and apply

### [1.10.0] - 2018-02-02

**Navigation for participants after confirmed estimation is shown**

* feat: remove reset button from confirmed estimation page
* feat: add active sessions link on confirmed estimation page
* feat: add return to issue link on confirmed estimation page
* feat: improve wording for confirmed estimation page
* style: use white space instead of tabs for templates

### [1.9.1] - 2018-01-26

**Log error when updating an issue fails with an error**

* docs: improve documentation for scrum poker session
* docs: fix credits to original author
* docs: fix sample for commit messages
* docs: add section about supported languages
* docs: add section with contribution ideas
* refactor: simplify verification custom field exists
* refactor: remove unused import
* refactor: log errors when updating issues
* test: verify issue update called after validation

### [1.9.0] - 2017-12-21

**Display Scrum Poker cards with CSS replacing previously used images**

* feat: cards are rendered by CSS and replace previously used images
* docs: add contibution guidelines
* fix: show questionmark on backside of cards
* fix: preserve styling for link after click on card
* fix: remove gap between active and closed sessions

### [1.8.0] - 2017-11-03

**Session overview shows issues based on user permissions**

* feat: added internationalization for error page
* feat: closed sessions link to issue instead of Scrum Poker session
* feat: sessions overview only shows sessions the user is allowed to see

### [1.7.2] - 2017-10-19

**Fixing session overview to display closed sessions even when no active sessions exist**

* fix: show closed sessions when no active sessions exist
* docs: improve changelog for better readability

### [1.7.1] - 2017-10-19

**Fixing missing translations on session overview**

* fix: add missing translations for English translation
* fix: correct number of headings for active sessions in session overview

### [1.7.0] - 2017-10-19

**Internationalization for all Scrum Poker pages and texts**

* feat: bump version of used Jira API to version 7.5.0
* feat: improve translations in German and English
* feat: complete internationalization of the plugin in German and English
* feat: layout improvements for page to display active Scrum Poker sessions

### [1.6.1] - 2017-09-08

**Internationalization for the active session page**

* feat: provide translation for the active sessions list
* chore: cleanup of plugin configuration
* chore: removed link to missing and unused javascript library

### [1.6.0] - 2017-08-22

**Display the name of the session starter in the session list**

* feat: show the name of the user who has started the session in the session list

### [1.5.1] - 2017-08-15

**Fixing the URL to images and resources for Jira installations without context path**

* fix: use base url to display images and links on estimation page

### [1.5.0] - 2017-08-15

**Display all sub tasks for the current session if sub tasks exist**

* feat: display sub tasks of the story currently being estimated

### [1.4.2] - 2017-08-11

**Update add on using the latest Atlassian SDK**

* refactor: extract settings handling into one component
* feat: implement against latest Atlassian Maven Jira Plugin

### [1.4.1] - 2017-08-08

**Sort active and recently closed Scrum Poker sessions by creation date**

* chore: cleanup of unused actions and imports, css selectors
* style: improve code formatting
* refactor: improve internal structure of action based code and remove redundancy
* feat: closed and open Scrum Poker sessions are sorted by creation date

### [1.4.0] - 2017-08-07

**Support for Atlassian Jira 7.3.6**

* refactor: use Java 8 language level
* feat: implement and test against Jira version 7.3.6

### [1.3.16] - 2017-06-21

**Remove Scrum Poker sessions after the configured timeout**

* feat: cleanup Scrum Poker Sessions older than 12 hours

### [1.3.15] - 2017-02-24

**Fixing access to the configuration to require a user with sysadmin role**

* fix: restrict access to configuration to sysadmin role

### [1.3.14] - 2017-02-24

**Improve configuration page to have valid and working configurations**

* feat: configuration of story point field is saved by id and not by name anymore (requires reconfiguration)
* feat: configuration page includes link to issue tracker on GitHub
* feat: configuration page has alert if no story point field ist configured

### [1.3.13] - 2017-02-16

**Allow to easily refresh the active session page**

* feat: overview page supports refreshing with button

### [1.3.12] - 2017-02-13

**Access the active session list from every Scrum Poker session**

* feat: overview page with all Scrum Poker sessions is accessible from currently opened session
* feat: overview page with all Scrum Poker sessions shows start date of every Scrum Poker session

### [1.3.11] - 2016-08-02

**Improve testing and include continuous integration system**

* chore: add Travis CI build

### [1.3.10] - 2016-08-02

* refactor: inject components instead of programmatically getting them
* refactor: bounded votes are only exposed through Scrum Poker session now
* style: overall cleanup (formatting and imports)

### [1.3.9] - 2016-07-22

**Bugfix for wrong hyperlinks on session overview page**

* fix: display correct URL in session overview

### [1.3.8] - 2016-07-22

**Open issue instead of session on sessions with estimation already agreed on**

* feat: navigate to issue for estimated Scrum Poker sessions in session overview
* refactor: new naming for Scrum Poker URLs reflecting the plugin name

### [1.3.7] - 2016-07-13

**First version of active session page**

* feat: add overview page with all currently active Scrum Poker sessions

### [1.3.6] - 2016-01-13

* refactor: improve cleanup of old Scrum Poker sessions

### [1.3.5] - 2016-01-08

* feat: Scrum Poker sessions can be started for all issue types with the configured custom field

### [1.3.4] - 2015-11-05

* feat: Scrum Poker sessions get cleaned up when older than one day
* feat: Participants see confirmed estimation when confirmed by one participant

### [1.3.3] - 2015-11-03

* feat: optimize internationalization for German and English users

### [1.3.2] - 2015-11-03

* feat: open new Scrum Poker sessions via URL

### [1.3.1] - 2015-11-02

* feat: poker card with value 0.5 removed from the Scrum Poker deck

### [1.3.0] - 2015-11-02

* feat: faster and more responsive interface during Scrum Poker session

### [1.2.3] - 2015-05-19

* feat: automatic refresh of poker cards in session every two seconds
* docs: added section with compatibility matrix in readme

### [1.2.2] - 2015-05-07

* refactor: extract votes logic into separate class and add tests
* feat: better approach to recognize referrer and lead back after poker session

### [1.2.1] - 2015-05-05

* feat: implement and test against Jira 6.4.3
* feat: rename Planning Poker Plugin to Scrum Poker Plugin
* refactor: simplify card image strategy using folders instead of file names
* refactor: introduce ScrumPokerAction as base for all action classes
* refactor: use IssueService instead of deprecated IssueManager
* refactor: rename configuration action according to other action classes
* style: use spaces instead of tabs and remove blank lines
* docs: add Apache License Version 2.0
* docs: improve readme and add description of the plugin
* docs: add javadoc to PlanningPokerStorage interface
* chore: add ignored file patterns for git version control
* chore: add Atlassian repository in POM to resolve Atlassian dependencies

### [1.2.0] - 2015-04-26

* fix: save and refresh estimation to be displayed in product backlog

### [1.1.0] - 2015-04-24

* feat: rename Planning Poker Plugin to Scrum Poker Plugin
* feat: reveal deck only if there are no new votes coming in
* feat: identify people with votes on the boundaries to engage talking to each other
* feat: allow resetting a deck after a Scrum Poker session
* feat: allow opening Scrum Poker from issue sidebar in Agile Board
* feat: add localization for German language
* feat: add filter to show and hide the Scrum Poker button based on issue types
* fix: single logic for naming the Scrum Poker estimations field
* fix: more robustness for filter to show and hide the Scrum Poker button based on issue types
* fix: encoding of German umlauts in German message bundle
* fix: invite to talk only when there are minimum two different estimations
* style: cleanup and formatting of initially generated POM
* style: global optimizations regarding formatting and code style

### [1.0.0] - 2015-04-23

**First runnable version of Scrum Poker**

* feat: initial usable version of Planning Poker Plugin

[Unreleased]: https://github.com/codescape/jira-scrum-poker/compare/4.8.2...HEAD
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
[3.13.0]: https://github.com/codescape/jira-scrum-poker/compare/3.12.0...3.13.0
[3.12.0]: https://github.com/codescape/jira-scrum-poker/compare/3.11.1...3.12.0
[3.11.1]: https://github.com/codescape/jira-scrum-poker/compare/3.11.0...3.11.1
[3.11.0]: https://github.com/codescape/jira-scrum-poker/compare/3.10.0...3.11.0
[3.10.0]: https://github.com/codescape/jira-scrum-poker/compare/3.9.0...3.10.0
[3.9.0]: https://github.com/codescape/jira-scrum-poker/compare/3.8.0...3.9.0
[3.8.0]: https://github.com/codescape/jira-scrum-poker/compare/3.7.0...3.8.0
[3.7.0]: https://github.com/codescape/jira-scrum-poker/compare/3.6.0...3.7.0
[3.6.0]: https://github.com/codescape/jira-scrum-poker/compare/3.5.0...3.6.0
[3.5.0]: https://github.com/codescape/jira-scrum-poker/compare/3.4.0...3.5.0
[3.4.0]: https://github.com/codescape/jira-scrum-poker/compare/3.3.0...3.4.0
[3.3.0]: https://github.com/codescape/jira-scrum-poker/compare/3.2.0...3.3.0
[3.2.0]: https://github.com/codescape/jira-scrum-poker/compare/3.1.2...3.2.0
[3.1.2]: https://github.com/codescape/jira-scrum-poker/compare/3.1.1...3.1.2
[3.1.1]: https://github.com/codescape/jira-scrum-poker/compare/3.1.0...3.1.1
[3.1.0]: https://github.com/codescape/jira-scrum-poker/compare/3.0.1...3.1.0
[3.0.1]: https://github.com/codescape/jira-scrum-poker/compare/3.0.0...3.0.1
[3.0.0]: https://github.com/codescape/jira-scrum-poker/compare/2.8.0...3.0.0
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
[1.12.0]: https://github.com/codescape/jira-scrum-poker/compare/1.11.0...1.12.0
[1.11.0]: https://github.com/codescape/jira-scrum-poker/compare/1.10.0...1.11.0
[1.10.0]: https://github.com/codescape/jira-scrum-poker/compare/1.9.1...1.10.0
[1.9.1]: https://github.com/codescape/jira-scrum-poker/compare/1.9.0...1.9.1
[1.9.0]: https://github.com/codescape/jira-scrum-poker/compare/1.8.0...1.9.0
[1.8.0]: https://github.com/codescape/jira-scrum-poker/compare/1.7.2...1.8.0
[1.7.2]: https://github.com/codescape/jira-scrum-poker/compare/1.7.1...1.7.2
[1.7.1]: https://github.com/codescape/jira-scrum-poker/compare/1.7.0...1.7.1
[1.7.0]: https://github.com/codescape/jira-scrum-poker/compare/1.6.1...1.7.0
[1.6.1]: https://github.com/codescape/jira-scrum-poker/compare/1.6.0...1.6.1
[1.6.0]: https://github.com/codescape/jira-scrum-poker/compare/1.5.1...1.6.0
[1.5.1]: https://github.com/codescape/jira-scrum-poker/compare/1.5.0...1.5.1
[1.5.0]: https://github.com/codescape/jira-scrum-poker/compare/1.4.2...1.5.0
[1.4.2]: https://github.com/codescape/jira-scrum-poker/compare/1.4.1...1.4.2
[1.4.1]: https://github.com/codescape/jira-scrum-poker/compare/1.4.0...1.4.1
[1.4.0]: https://github.com/codescape/jira-scrum-poker/compare/1.3.16...1.4.0
[1.3.16]: https://github.com/codescape/jira-scrum-poker/compare/1.3.15...1.3.16
[1.3.15]: https://github.com/codescape/jira-scrum-poker/compare/1.3.14...1.3.15
[1.3.14]: https://github.com/codescape/jira-scrum-poker/compare/1.3.13...1.3.14
[1.3.13]: https://github.com/codescape/jira-scrum-poker/compare/1.3.12...1.3.13
[1.3.12]: https://github.com/codescape/jira-scrum-poker/compare/1.3.11...1.3.12
[1.3.11]: https://github.com/codescape/jira-scrum-poker/compare/1.3.10...1.3.11
[1.3.10]: https://github.com/codescape/jira-scrum-poker/compare/1.3.9...1.3.10
[1.3.9]: https://github.com/codescape/jira-scrum-poker/compare/1.3.8...1.3.9
[1.3.8]: https://github.com/codescape/jira-scrum-poker/compare/1.3.7...1.3.8
[1.3.7]: https://github.com/codescape/jira-scrum-poker/compare/1.3.6...1.3.7
[1.3.6]: https://github.com/codescape/jira-scrum-poker/compare/1.3.5...1.3.6
[1.3.5]: https://github.com/codescape/jira-scrum-poker/compare/1.3.4...1.3.5
[1.3.4]: https://github.com/codescape/jira-scrum-poker/compare/1.3.3...1.3.4
[1.3.3]: https://github.com/codescape/jira-scrum-poker/compare/1.3.2...1.3.3
[1.3.2]: https://github.com/codescape/jira-scrum-poker/compare/1.3.1...1.3.2
[1.3.1]: https://github.com/codescape/jira-scrum-poker/compare/1.3.0...1.3.1
[1.3.0]: https://github.com/codescape/jira-scrum-poker/compare/1.2.3...1.3.0
[1.2.3]: https://github.com/codescape/jira-scrum-poker/compare/1.2.2...1.2.3
[1.2.2]: https://github.com/codescape/jira-scrum-poker/compare/1.2.1...1.2.2
[1.2.1]: https://github.com/codescape/jira-scrum-poker/compare/1.2.0...1.2.1
[1.2.0]: https://github.com/codescape/jira-scrum-poker/compare/1.1.0...1.2.0
[1.1.0]: https://github.com/codescape/jira-scrum-poker/compare/1.0.0...1.1.0
[1.0.0]: https://github.com/codescape/jira-scrum-poker/tree/1.0.0
