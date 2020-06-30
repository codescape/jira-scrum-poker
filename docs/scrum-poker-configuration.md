---
layout: default
title: Scrum Poker configuration
category: Administration
---

This page describes how to configure Scrum Poker for Jira to work in your Jira application.

* Table of Contents
{:toc}

### Global configuration

Scrum Poker for Jira has some global configuration parameters that can be configured from the administration section of your Jira application.
This configuration is only available to users with the `Jira Administrators` role assigned.

To locate the global configuration navigate to the `Administration` menu, then select the entry labelled `Manage apps`.
You will now find a section called `Scrum Poker` in the left sidebar as shown in the screenshot below:

![Locate the global configuration for Scrum Poker](/images/scrum-poker-configuration-locate-page.png)

On the page `Configuration` you have global configuration options to make Scrum Poker for Jira fit to your needs:

![Global configuration for Scrum Poker](/images/scrum-poker-configuration-page.png)

#### Confirmed Estimate Field

Select the custom field that is used to persist the confirmed estimate when completing a Scrum Poker session.

Jira Software creates the field `Story Points` for estimates.
Since this field is translated into your Jira system language it might have another label.
In a German Jira instance this field is called `Story Punkte` for example.

It is preferred to choose this field because estimates that are saved into this field are displayed in the estimate badge on the backlog of agile boards.

Scrum Poker for Jira currently supports the following field types to be chosen in the configuration and be filled with the confirmed estimate:

* Number Field
* Text Field (multi-line)
* Text Field (single line)

Please [let us know](/support) if you require other field types to be supported.

#### Estimate Permission Check

Decide whether every participant of a Scrum Poker session is allowed to save the estimate or permission to edit the issue shall be required for the current user.

* If disabled all participants of the Scrum Poker session regardless of their permissions on the current issue can confirm the estimate and end the Scrum Poker session.
* If enabled only users with edit permission on the current issue the Scrum Poker session is started for can confirm the estimate and end the Scrum Poker session.

#### Card Set

Define the default card set to be used for Scrum Poker sessions.
Use a comma-separated list of values.
Special cards are defined by the keywords `coffee` (the user needs a break) and `question` (the user is not able to estimate).

The default card set that is preconfigured is the simplified Fibonacci sequence:

    question, coffee, 0, 1, 2, 3, 5, 8, 13, 20, 40, 100

When you define your own card sets always keep in mind that only numeric values can be confirmed and assigned to the `Story Points` field created by Jira Software.

#### Session Timeout

Adjust the session timeout of Scrum Poker sessions in hours.
Sessions older than this timeout will not be shown on the `Active Sessions` page to keep this page clean and intuitive.

The default value is set to `12 hours` which implies that sessions of the last half day are shown on the `Active Sessions` page.

#### Activate Scrum Poker

Decide whether Scrum Poker is activated globally for all projects.
If not activated globally this global setting can be overridden per project in the project settings.

* If activated Scrum Poker sessions can be started for all issues in all Jira projects that have the `Confirmed Estimate Field` configured.
* If not activated Scrum Poker sessions can only be started for issues in those projects that have Scrum Poker explicitly activated in the `Project Configuration` and have the `Confirmed Estimate Field` configured.

#### Allow Reveal Deck

Decide who is allowed to reveal the votes and make them visible during a Scrum Poker session. You can choose from the following options:

* `EVERYONE` - Everyone is allowed to reveal the deck.
* `PARTICIPANTS` - Only participants who have provided a vote are allowed to reveal the deck.
* `CREATOR` - Only the creator of the session is allowed to reveal the deck.

#### Show Comments

Decide whether comments on the issue will be displayed in the Scrum Poker session.
Some teams use comments to communicate and discuss details of an issue and so those comments might help when estimating an issue.

The following options are available:

* `Show all comments` allows participants of a Scrum Poker session to see all comments they are allowed to see.
* `Show latest comments` allows participants of a Scrum Poker session to see the most recent ten comments they are allowed to see.
* `Show no comments` removes the section that displays comments on the Scrum Poker session page and thus shows no comments.

#### Show Dropdown on Boards

Decide whether to show the Scrum Poker dropdown on boards or not.
In older versions of Scrum Poker this dropdown was the only way to access the list of active Scrum Poker sessions.
Today the Scrum Poker menu elements in the Jira top menu `Boards` are preferred.

By default this option is disabled as this feature is just a convenience feature for users of older Scrum Poker versions who want to use this way to open the `Active Sessions` page.

### Project configuration

Scrum Poker for Jira also provides project specific configuration.

The locate the project specific configuration navigate to the project and find the `Project settings` link in the bottom of the left menu.
Inside the `Project settings` you can find a menu option called `Scrum Poker` that leads to the configuration.

On the page `Project Configuration` you can find the configuration options the make Scrum Poker for Jira fit to the needs of your Jira project:

![Project configuration for Scrum Poker](/images/scrum-poker-configuration-project-page.png)

#### Activate Scrum Poker

If Scrum Poker is not activated for all projects with the `Activate Scrum Poker` setting in the global settings this project specific setting allows you to activate Scrum Poker explicitly for the current project.

### Workflow configuration

Scrum Poker for Jira provides functions to interact with Scrum Poker from your Jira workflows.

#### Start Scrum Poker session

Scrum Poker offers the workflow function `Start Scrum Poker session` to start a Scrum Poker session on a workflow transition under the following conditions:

* a valid Scrum Poker for Jira license exists
* the issue is estimable
* there is no active session already started for this issue

Inside your workflow configuration you can add so called `Post Functions` to a workflow transition (see screenshot below).

![Start Scrum Poker session](/images/scrum-poker-configuration-start-session-workflow-function.png)

After adding this workflow function remember to publish the changed workflow.
Changes to workflows are only applied to future workflow transitions using this workflow if the workflow configuration is published.

If required you can easily remove the workflow function `Start Scrum Poker session` from the `Post Functions` overview panel again.
