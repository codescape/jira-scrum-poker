# Scrum Poker

This plugin provides a Scrum poker solution for JIRA that integrates well with the JIRA Agile estimation custom field which is used to calculate the velocity of a sprint.

## Getting Started

The Atlassian Plugin SDK provides several useful commands:

* `atlas-run` installs this plugin into the product and starts it on localhost
* `atlas-debug` same as atlas-run, but allows a debugger to attach at port 5005
* `atlas-cli` after atlas-run or atlas-debug, opens a Maven command line window:
  * `pi` reinstalls the plugin into the running product instance
* `atlas-help` prints description for all commands in the SDK

For further documentation see: [Set up the Atlassian SDK and build a project](https://developer.atlassian.com/docs/getting-started/set-up-the-atlassian-plugin-sdk-and-build-a-project)

## Compatibility

Due to deprecated classes in older versions of the Atlassian SDK, starting with version 1.2.1 Scrum Poker is only compatible to Atlassian JIRA 6.4+.


## Further documentation

* Finding the right Web Item https://marketplace.atlassian.com/plugins/com.wittified.webfragment-finder