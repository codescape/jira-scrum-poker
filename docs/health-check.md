---
layout: default
title: Health Check
category: Administration
---

This page describes how to perform a basic health check for Scrum Poker for Jira to find potential problems with the license or the configuration.

* Table of Contents
{:toc}

### Locate and use

To locate the health check navigate to the `Administration` menu, then select the entry labelled `Manage apps`.
You will now find a section called `Scrum Poker` in the left sidebar as shown in the screenshot below:

![Locate and use the Health Check](/images/health-check-locate-and-use.png)

To start a health check you can select the checks to be performed but running all checks is always recommended.

After clicking `Start Health Check` the selected checks are performed and results are shown below.
Positive results come with a checkmark and a green border.
Negative results are displayed with a red border.

### List of checks

The following checks are implemented and performed when started:

* License Check
  * Check that a license is configured
  * Check that the license is not expired
  * Check that the license is valid (size, version, type)

* Configuration Check
  * Check that the estimate field is configured
  * Check that the configured estimate field exists in your Jira instance
  * Check that the configured estimate field has a supported field type 
  * Check that Scrum Poker is either globally activated or has projects explicitly activated
  * Check that the configured card set has no obvious problems

* Error Log Check
  * Check that the error log is empty
