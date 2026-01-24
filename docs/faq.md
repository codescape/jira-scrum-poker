---
layout: default
title: Frequently Asked Questions
category: User Guide
---

Some questions regarding Scrum Poker for Jira might not be answered in this comprehensive documentation.
Find those questions collected and answered on this page.

Your question is not answered here? Please [get in contact](/support) with us.

* Table of Contents
{:toc}

### Why is the standard Original Estimate field not supported by Scrum Poker for Jira?

Scrum Poker for Jira advocates collaborative estimations using Story Points or any other measure that comes to your mind.
Those estimates represent the ideas of complexity and amount of work but should not be converted into some time duration unit as displayed in the Original Estimate field.

### Will there be a Jira Cloud version of Scrum Poker for Jira?

At the moment only Jira Server and Jira Data Center compatible versions of Scrum Poker for Jira are available.
We have already started to develop a Jira Cloud version of Scrum Poker for Jira.
A first release will be announced as soon as it is available.

### Do Scrum Poker sessions automatically end?

Yes, sessions older than the configured `Session Timeout` are automatically cancelled.
A new session can be started again when needed.

### Where does Scrum Poker for Jira persist data?

Scrum Poker for Jira does not persist any data outside your instance.
All data persisted by Scrum Poker for Jira can be found in the following database tables:

* `AO_1FA2A8_SCRUM_POKER_ERROR`
* `AO_1FA2A8_SCRUM_POKER_PROJECT`
* `AO_1FA2A8_SCRUM_POKER_SESSION`
* `AO_1FA2A8_SCRUM_POKER_SETTING`
* `AO_1FA2A8_SCRUM_POKER_VOTE`
