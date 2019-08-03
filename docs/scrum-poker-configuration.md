---
layout: default
title: Scrum Poker configuration
---

This page describes how to configure Scrum Poker for Jira to work in your Jira application.

* Table of Contents
{:toc}

### Global Configuration

Scrum Poker for Jira has some global configuration parameters that can be configured from the administration section of your Jira application.

To locate the global configuration navigate to the `Administration` menu, then select the entry labelled `Manage apps`.
You will now find a section called `Scrum Poker` in the left sidebar as shown in the screenshot below:

![Locate the global configuration for Scrum Poker](/images/scrum-poker-configuration-locate-page.png) 

On the page `Configuration` you have global configuration options to make Scrum Poker for Jira fit to your needs:

![Global configuration for Scrum Poker](/images/scrum-poker-configuration-page.png) 

#### Confirmed Estimation Field

Select the custom field that is used to persist the confirmed estimations when completing a Scrum Poker session.

Jira Software creates the field `Story Points` for estimations.
Since this field is translated into your Jira system language it might have another label.
In a German Jira instance this field is called `Story Punkte` for example.

It is preferred to choose this field because estimations that are saved into this field are displayed in the estimation badge on the backlog of agile boards.

#### Estimation Permission Check

Decide whether every participant of a Scrum Poker session is allowed to save the estimation or permission to edit the issue shall be required for the current user.

* If disabled all participants of the Scrum Poker session regardless of their permissions on the current issue can confirm the estimation and end the Scrum Poker session.
* If enabled only users with edit permission on the current issue the Scrum Poker session is started for can confirm the estimation and end the Scrum Poker session.

#### Session Timeout

Adjust the session timeout of Scrum Poker sessions in hours.
Sessions older than this timeout will not be shown on the `Active Sessions` page to keep this page clean and intuitive.

The default value is set to `12 hours` which implies that sessions of the last half day are shown on the `Active Sessions` page.

#### Default Project Activation

Decide whether Scrum Poker is enabled or disabled for all projects per default.
This global setting can be overridden per project in the project settings.

* If enabled Scrum Poker sessions can be started for all issues in all Jira projects that have the `Confirmed Estimation Field` configured.
* If disabled Scrum Poker sessions can only be started for issues in those projects that have Scrum Poker explicitly enabled in the `Project Configuration` and have the `Confirmed Estimation Field` configured. 

#### Allow Reveal Deck

Decide who is allowed to reveal the votes and make them visible during a Scrum Poker session. You can choose from the following options:

* `EVERYONE` - Everyone is allowed to reveal the deck.
* `PARTICIPANTS` - Only participants who have provided a vote are allowed to reveal the deck.
* `CREATOR` - Only the creator of the session is allowed to reveal the deck.

#### Show Dropdown on Boards

Decide whether to show the Scrum Poker dropdown on boards or not.
In older versions of Scrum Poker this dropdown was the only way to access the list of active Scrum Poker sessions.
Today the Scrum Poker menu elements in the Jira top menu `Boards` are preferred.

By default this option is disabled as this feature is just a convenience feature for users of older Scrum Poker versions who want to use this way to open the `Active Sessions` page. 

### Project Configuration

Scrum Poker for Jira also provides project specific configuration.

The locate the project specific configuration navigate to the project and find the `Project settings` link in the bottom of the left menu.
Inside the `Project settings` you can find a menu option called `Scrum Poker` that leads to the configuration.

On the page `Project Configuration` you can find the configuration options the make Scrum Poker for Jira fit to the needs of your Jira project:

![Project configuration for Scrum Poker](/images/scrum-poker-configuration-project-page.png) 

#### Enable Scrum Poker

If Scrum Poker is not enabled for all projects with the `Default Project Activation` setting in the global settings this project specific setting allows you to enable Scrum Poker explicitly for the current project.
