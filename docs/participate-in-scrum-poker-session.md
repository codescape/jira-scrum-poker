---
layout: default
title: Participate in a Scrum Poker session
category: User Guide
---

This page describes how to participate in a Scrum Poker session.

* Table of Contents
{:toc}

### Understanding the page structure

Once you have [joined a Scrum Poker session](/join-scrum-poker-session) you can see the Scrum Poker session page.
This page is divided horizontally into two sections:

* On the left side you can see details about the issue.
This includes the full issue description and a list of all subtasks that exist for that issue.
With this information at hand you can provide your own estimate.
* On the right side you can find all elements to interact with the Scrum Poker session.

![Layout of the Scrum Poker session page](/images/participate-in-scrum-poker-session-page-layout.png)

The process of a Scrum Poker session normally looks like this:

* All participants provide their estimate by choosing a card
* Once all participants have provided their estimate the cards are revealed
* When agreeing on an estimate one participant confirms the estimate, otherwise a new round can be started

### Provide your estimate

All participants can choose from the cards listed under the heading `Scrum Poker` by clicking on the card that reflects their estimate.
While moving your mouse cursor over the cards previous Scrum Poker sessions you have participated in are shown to let you compare the new issue with other issues with the same estimate.

![Compare with previously estimated issues](/images/participate-in-scrum-poker-session-compare-estimates.png)

As you can see in the screenshot above when you have chosen a card it is highlighted and your name appears in the list of participants where the chosen card values are not shown to other participants yet.


### Reveal all estimates

Once all participants have provided their estimate the cards can be revealed by clicking on the `Reveal` button.

Scrum Poker for Jira will then show the `Range of votes` and mark all participants in the `Participants` list who provided a vote for an estimate on the boundaries of all estimates.
This can be seen as an invitation to talk about their reason for choosing that estimate.

![Reveal all estimates](/images/participate-in-scrum-poker-session-reveal-estimates.png)

### Confirm agreed estimate

If all participants agree on an estimate one participant can confirm the estimate in clicking on the card in the `Range of votes` list.
This saves the chosen estimate on the issue and informs all participants about the completed estimation process.

![Confirmed estimate](/images/participate-in-scrum-poker-session-confirmed-estimate.png)

If the feature `Estimation Permission Check` (see [Configuration](/configuration)) is enabled users who have no edit rights for the given issue see all estimations in the `Range of votes` grayed out.
Only those users who are allowed to save the estimation for that issue see blue clickable estimation cards to confirm the estimation.
