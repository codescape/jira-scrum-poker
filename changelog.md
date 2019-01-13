# Changelog

This changelog helps developers and users to keep track of new features, fixes and improvements for the Scrum Poker plugin.

## [Unreleased]

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

## [3.7.0] - 2018-12-03

* test: cleanup test for REST endpoint and remove unnecessary test data
* test: cleanup test for automatic upgrade task and remove unnecessary test data
* test: improve naming of test methods to better reflect test case
* feat: improve error page and error massage in case of an error
* feat: automatically install Jira Software in development mode
* refactor: retrieve current user in separate and reusable method
* chore: implement against latest version of Atlassian SDK
* test: add test for correct behaviour when cancelling a session
* chore: implement against latest Jira patch version 7.13.0
 
## [3.6.0] - 2018-09-26

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

# [3.5.0] - 2018-09-16

* docs: improve online documentation layout for mobile devices
* feat: make Scrum Poker accessible from Jira Software boards menu
* docs: update documentation for joining an active session
* refactor: use links starting with uppercase for Scrum Poker
* feat: move Scrum Poker configuration into database (#31)
* test: simplify test and remove unnecessary test data
* feat: add parameter to signal Atlassian Data Center compatibility (#24)
* feat: automatically migrate old configuration into new one and cleanup
* feat: layout improvements for Scrum Poker session to better guide users

## [3.4.0] - 2018-09-15

* fix: correct escape for umlauts in configuration description
* chore: implement against latest Jira version
* refactor: rename Scrum poker session service implementation
* feat: prevent issue description from floating into poker elements
* refactor: change primary key for persisted votes from int to Long
* feat: do not create Scrum poker sessions for issues missing estimation field
* test: use Mockito ArgumentMatcher instead of deprecated Mockito Matcher
* test: improve tests and remove unnecessary details in setup 
* chore: implement against latest Jira patch version 7.12.1
* feat: sort buttons on Scrum Poker session page by usage frequency
* docs: implement a first version of an online documentation

## [3.3.0] - 2018-09-01

* feat: ensure minimum value of zero for session timeout configuration
* feat: show warning/success message when saving estimation to issue
* docs: add inline documentation to CSS code
* feat: improve error message when saving estimation to issue
* test: improve log output for better readability

## [3.2.0] - 2018-08-10

* feat: Scrum Poker session timeout configurable in plugin settings (#19)
* feat: allow the creator of a session to cancel the session (#20)
* chore: implement against latest Jira patch version 7.11.2
* fix: button "Start Scrum Poker" is shown twice (#28)
* feat: improve readability of global plugin configuration
* refactor: use Java 8 lambdas and use null save operations where appropriate

## [3.1.2] - 2018-07-27

* docs: improve privacy statement with details to data usage
* fix: cancel fading out reference issues when references shall be displayed
* fix: hide references immediately when estimation is confirmed
* fix: do not show references where issue does not exist anymore
* chore: implement against latest Jira patch version 7.11.1
* feat: simplify add-on global configuration page

## [3.1.1] - 2018-07-18

* fix: do not fail to display session list for not existing issues
* fix: do not create sessions for not existing issues
* feat: reduce requests to reference issues and fade out slowly in gui
* refactor: improve code for persistence service

## [3.1.0] - 2018-07-17

* feat: show reference issues during session to help find the estimation
* refactor: improve code to transform database model into the view model
* refactor: rename estimation field service and use dependency injection
* refactor: rename settings service and use dependency injection

## [3.0.1] - 2018-07-16

* chore: implement against Jira version 7.11.x
* fix: do announce agreement only for revealed sessions
* fix: hide the revealed session when new votes are added (#23)

## [3.0.0] - 2018-07-13

* chore: implement against latest Jira version 7.10.2
* test: add tests for Scrum Poker configuration action
* refactor: rename and move condition to display Scrum poker for issue
* refactor: use better base class for condition to render Scrum Poker button
* feat: add link to marketplace on plugin configuration page
* refactor: introducing persistence to Scrum poker session with Active Objects

## [2.8.0] - 2018-06-01

* feat: Scrum Poker is now provided for free on Atlassian Marketplace

## [2.7.0] - 2018-05-17

* fix: remove small typo in privacy statement
* feat: show display name of assignee in list of sub tasks
* feat: display the list of sub tasks equally to the issue detail page
* chore: use latest version von Jira and Lucene

## [2.6.0] - 2018-05-13

* feat: use buttons and Jira default styling where appropriate
* docs: changelog includes links to GitHub comparison between tags
* docs: improve Credits section in Contributing document
* docs: add Code of Conduct file
* docs: add Releases section in Contributing documentation
* feat: less text same meaning for session list headings
* docs: provide better screenshots and show more situations
* refactor: move REST entities into separate package
* refactor: rename REST entities to *Entity
* refactor: use Jackson annotations for JSON entity classes
* docs: provide Privacy Statement for Scrum Poker

## [2.5.0] - 2018-04-29

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

## [2.4.1] - 2018-04-20

* fix: estimation of 0 can be confirmed correctly
* refactor: improved naming convention for REST representations
* docs: update and improve general description of the Scrum Poker plugin
* docs: update and improve installation and compatibility description

## [2.4.0] - 2018-04-16

* feat: add post update and post install urls to improve setup process
* refactor: move all persistence related classes into persistence package
* feat: create own ScrumPoker namespace for JavaScript code
* docs: improve readme and add installation instructions
* style: formatting the JavaScript file
* feat: rename to Scrum Poker to adhere to Atlassian brand guidelines 

## [2.3.0] - 2018-04-11

* style: tabs and spaces for session template
* docs: update screenshots and description in readme
* feat: configuration accessible from plugin administration sidebar
* feat: remove logging while accessing story point field
* feat: introduce licensing for the Scrum Poker plugin
* refactor: introduce better naming for plugin components

## [2.2.0] - 2018-03-26

* feat: after session is closed stop polling for updates
* style: format error template
* style: format session template

## [2.1.1] - 2018-03-26

* fix: always use correct path for REST requests

## [2.1.0] - 2018-03-26

* refactor: move cards list to enum
* refactor: remove obsolete base class
* feat: question mark always has to talk
* feat: add new logo for Scrum Poker plugin
* fix: correct level for configuration heading

## [2.0.1] - 2018-03-24

* test: add tests for REST endpoint
* fix: agreement should not ignore question marks

## [2.0.0] - 2018-03-24

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

## [1.12.0] - 2018-03-11

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

## [1.11.0] - 2018-02-27

* feat: stop users from opening issues in Scrum Poker they may not see
* feat: add robustness when a key cannot be resolved to a user
* feat: improve error page which will now also appear when user is not allowed to see issue
* refactor: simplify refreshing session data and remove unused code
* docs: fix typo and remove trailing whitespace
* style: add initial version of EditorConfig file
* style: add indent and charset to EditorConfig and apply

## [1.10.0] - 2018-02-02

* feat: remove reset button from confirmed estimation page
* feat: add active sessions link on confirmed estimation page
* feat: add return to issue link on confirmed estimation page
* feat: improve wording for confirmed estimation page
* style: use white space instead of tabs for templates

## [1.9.1] - 2018-01-26

* docs: improve documentation for scrum poker session
* docs: fix credits to original author
* docs: fix sample for commit messages
* docs: add section about supported languages
* docs: add section with contribution ideas 
* refactor: simplify verification custom field exists
* refactor: remove unused import
* refactor: log errors when updating issues
* test: verify issue update called after validation

## [1.9.0] - 2017-12-21

* feat: cards are rendered by CSS and replace previously used images
* docs: add contibution guidelines
* fix: show questionmark on backside of cards
* fix: preserve styling for link after click on card
* fix: remove gap between active and closed sessions 

## [1.8.0] - 2017-11-03

* feat: added internationalization for error page
* feat: closed sessions link to issue instead of Scrum Poker session
* feat: sessions overview only shows sessions the user is allowed to see

## [1.7.2] - 2017-10-19

* fix: show closed sessions when no active sessions exist
* docs: improve changelog for better readability

## [1.7.1] - 2017-10-19

* fix: add missing translations for English translation
* fix: correct number of headings for active sessions in session overview

## [1.7.0] - 2017-10-19

* feat: bump version of used Jira API to version 7.5.0
* feat: improve translations in German and English
* feat: complete internationalization of the plugin in German and English
* feat: layout improvements for page to display active Scrum Poker sessions

## [1.6.1] - 2017-09-08

* feat: provide translation for the active sessions list
* chore: cleanup of plugin configuration
* chore: removed link to missing and unused javascript library

## [1.6.0] - 2017-08-22

* feat: show the name of the user who has started the session in the session list

## [1.5.1] - 2017-08-15

* fix: use base url to display images and links on estimation page

## [1.5.0] - 2017-08-15

* feat: display sub tasks of the story currently being estimated

## [1.4.2] - 2017-08-11

* refactor: extract settings handling into one component
* feat: implement against latest Atlassian Maven Jira Plugin

## [1.4.1] - 2017-08-08

* chore: cleanup of unused actions and imports, css selectors 
* style: improve code formatting
* refactor: improve internal structure of action based code and remove redundancy
* feat: closed and open Scrum poker sessions are sorted by creation date

## [1.4.0] - 2017-08-07

* refactor: use Java 8 language level
* feat: implement and test against Jira version 7.3.6

## [1.3.16] - 2017-06-21

* feat: cleanup Scrum Poker Sessions older than 12 hours

## [1.3.15] - 2017-02-24

* fix: restrict access to configuration to sysadmin role

## [1.3.14] - 2017-02-24

* feat: configuration of story point field is saved by id and not by name anymore (requires reconfiguration)
* feat: configuration page includes link to issue tracker on GitHub
* feat: configuration page has alert if no story point field ist configured

## [1.3.13] - 2017-02-16

* feat: overview page supports refreshing with button

## [1.3.12] - 2017-02-13

* feat: overview page with all Scrum Poker sessions is accessible from currently opened session
* feat: overview page with all Scrum Poker sessions shows start date of every Scrum Poker session

## [1.3.11] - 2016-08-02

* chore: add Travis CI build

## [1.3.10] - 2016-08-02

* refactor: inject components instead of programmatically getting them
* refactor: bounded votes are only exposed through Scrum Poker session now
* style: overall cleanup (formatting and imports)

## [1.3.9] - 2016-07-22

* fix: display correct URL in session overview

## [1.3.8] - 2016-07-22

* feat: navigate to issue for estimated Scrum Poker sessions in session overview
* refactor: new naming for Scrum Poker URLs reflecting the plugin name

## [1.3.7] - 2016-07-13

* feat: add overview page with all currently active Scrum Poker sessions

## [1.3.6] - 2016-01-13

* refactor: improve cleanup of old Scrum Poker sessions

## [1.3.5] - 2016-01-08

* feat: Scrum Poker sessions can be started for all issue types with the configured custom field

## [1.3.4] - 2015-11-05

* feat: Scrum Poker sessions get cleaned up when older than one day
* feat: Participants see confirmed estimation when confirmed by one participant

## [1.3.3] - 2015-11-03

* feat: optimize internationalization for German and English users

## [1.3.2] - 2015-11-03

* feat: open new Scrum Poker sessions via URL

## [1.3.1] - 2015-11-02

* feat: poker card with value 0.5 removed from the Scrum Poker deck

## [1.3.0] - 2015-11-02

* feat: faster and more responsive interface during Scrum Poker session

## [1.2.3] - 2015-05-19

* feat: automatic refresh of poker cards in session every two seconds 
* docs: added section with compatibility matrix in readme

## [1.2.2] - 2015-05-07

* refactor: extract votes logic into separate class and add tests
* feat: better approach to recognize referrer and lead back after poker session

## [1.2.1] - 2015-05-05

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

## [1.2.0] - 2015-04-26

* fix: save and refresh estimation to be displayed in product backlog

## [1.1.0] - 2015-04-24

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

## [1.0.0] - 2015-04-23

* feat: initial usable version of Planning Poker Plugin

[Unreleased]: https://github.com/codescape/jira-scrum-poker/compare/3.7.0...HEAD
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
