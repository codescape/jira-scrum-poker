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
You will now find a section called `Scrum Poker for Jira` in the left sidebar as shown in the screenshot below:

![Locate the global configuration for Scrum Poker](/images/scrum-poker-configuration-locate-page.png) 

#### Configuration Options

On the page `Configuration` you have global configuration options to make Scrum Poker for Jira fit to your needs:

![Global configuration for Scrum Poker](/images/scrum-poker-configuration-page.png) 

##### Confirmed Estimation Field

Select the custom field that is used to persist the confirmed estimations when completing a Scrum Poker session.

Jira Software creates the field `Story Points` for estimations.
Since this field is translated into your Jira system language it might have another label.
In a German Jira instance this field is called `Story Punkte` for example.

It is preferred to choose this field because estimations that are saved into this field are displayed in the estimation badge on the backlog of agile boards.

##### Session Timeout

Adjust the session timeout of Scrum Poker sessions in hours.
Sessions older than this timeout will not be shown on the `Active Sessions` page to keep this page clean and intuitive.

##### Default Project Activation

Decide whether Scrum Poker is enabled or disabled for all projects per default. This global setting can be overridden per project in the project settings.

##### Allow Reveal Deck

Decide who is allowed to reveal the votes and make them visible during a Scrum Poker session. You can choose from the following options:

* EVERYONE - Everyone is allowed to reveal the deck.
* PARTICIPANTS - Only participants who have provided a vote are allowed to reveal the deck.
* CREATOR - Only the creator of the session is allowed to reveal the deck.

### Project Configuration

Scrum Poker for Jira also provides project specific configuration.

The locate the project specific configuration navigate to the project and find the `Project settings` link in the bottom of the left menu. Inside the `Project settings` you can find a menu option called `Scrum Poker for Jira` that leads to the configuration.

#### Configuration Options

On the page `Project Configuration` you can find the configuration options the make Scrum Poker for Jira fit to the needs of your Jira project:

![Project configuration for Scrum Poker](/images/scrum-poker-configuration-project-page.png) 

##### Enable Scrum Poker

If Scrum Poker is not enabled for all projects per default in the global settings this setting allows you to enable Scrum Poker individually for the current project.
