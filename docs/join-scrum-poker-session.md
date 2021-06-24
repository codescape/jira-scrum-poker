---
layout: default
title: Join a Scrum Poker session
category: User Guide
---

This page describes how to join a currently running Scrum Poker session.

* Table of Contents
{:toc}

### Preconditions

A Scrum Poker session can only be joined by users who may see the issue for that the Scrum Poker session is started.


### Choose your preferred way

To join a currently running Scrum Poker session there are basically three ways in doing so.

#### Find your session in the Active Sessions list

The preferred and easiest way is the so called `Active Sessions` page which can be accessed from the main navigation menu `Boards` provided by Jira Software where you can find another `Active Sessions` link.

This page can also be accessed from the `Scrum Poker` dropdown that is visible on every agile board in Jira. This dropdown might not be shown depending on the `Show Dropdown on Boards` setting as explained on the [Configuration](/configuration) page.

![Navigate to the active sessions page](/images/start-scrum-poker-session-open-active-sessions.png)

The `Active Session` page displays all currently running and recently finished sessions related to issues that are visible to you.
It allows you to directly jump into all running sessions.

![Join a Scrum Poker session from the active sessions page](/images/join-scrum-poker-session-active-sessions.png)

#### Join the session from the Issue detail page

As an alternative you can use all the ways described on the page showing how to [Start a Scrum Poker session](/start-scrum-poker-session), too.
The button is not labelled `Start Scrum Poker` when an active session exists but displays as `Join Scrum Poker` in this case.

#### Join a session through scanning QR code from the session page

If your team is in the same room and wants to use mobile devices to join a Scrum Poker session they can scan the provided QR code available from the `Share Session` dropdown on the session page for example visible to all participants on a monitor or projector.  

![Join a Scrum Poker session scanning the provided QR code](/images/join-scrum-poker-session-qr-code.png)

#### Find active Scrum Poker sessions using JQL

If you are familiar with JQL (the Jira Query Language) you can use a JQL function provided by Scrum Poker for Jira to search for active Scrum Poker sessions.
Scrum Poker for Jira comes with the JQL function `issue in activeScrumPokerSessions()` that can be combined with all your queries and restricts the results to issues with active Scrum Poker sessions.

Example: Let us assume you want to list all active Scrum Poker sessions in a project with the project key `DEMO`. The query would be `project = DEMO and issue in activeScrumPokerSessions()` and it would return all issues with an active Scrum Poker session in the project called DEMO.
