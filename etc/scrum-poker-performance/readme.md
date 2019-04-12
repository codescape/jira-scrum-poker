# Scrum Poker Performance Tests

This project holds all functionality to run the Jira Data Center performance tests.

## Setting up the Cluster

Docker images from this respository are used to set up and scale the custer:
https://github.com/codeclou/docker-atlassian-jira-data-center

## Running the Tests

Use `mvn verify` to run the tests with the default configuration.
The default configuration can be overridden.
See the `pom.xml` for all possible configuration options.

Example: To change the baseUrl use this command:

    mvn verify -DbaseUrl=http://localhost:2990/jira/

## Order of Tests

Produce the results by following this order:

* Cluster with one node without Scrum Poker first then with Scrum Poker second
* Cluster with two nodes without Scrum Poker first then with Scrum Poker second
* Cluster with four nodes without Scrum Poker first then with Scrum Poker second
