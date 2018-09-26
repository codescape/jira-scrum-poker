---
layout: default
title: Scrum Poker configuration
---

This page describes how to configure the Scrum Poker add-on to work in your Jira application.

* Table of Contents
{:toc}

### Global Configuration

The Scrum Poker add-on has some global configuration parameters that can be configured from the administration section of your Jira application.

To locate the global configuration navigate to the `Administration` menu, then select the entry labelled `Add-ons`.
You will now find a section called `Scrum Poker` in the left sidebar as shown in the screenshot below:

![Locate the global configuration for Scrum Poker](/images/scrum-poker-configuration-locate-page.png) 

### Configuration options

On the page `Configuration` you have two configuration options to make the Scrum Poker add-on fit to your needs:

![Global configuration for Scrum Poker](/images/scrum-poker-configuration-page.png) 

#### Field for confirmed estimation

Select the custom field that is used to persist the confirmed estimations when completing a Scrum Poker session.

Jira Software creates the field `Story Points` for estimations.
Since this field is translated into your Jira system language it might have another label.
In a German Jira instance this field is called `Story Punkte` for example.

It is preferred to choose this field because estimations that are saved into this field are displayed in the estimation badge on the backlog of agile boards.

#### Session Timeout

Adjust the session timeout of Scrum Poker sessions in hours.
Sessions older than this timeout will not be shown on the `Active Sessions` page to keep this page clean and intuitive.
