# Scrum Poker Performance Tests

This project holds all functionality to run the Jira Data Center performance tests.

## Running the Tests

Use `mvn verify` to run the tests with the default configuration.
The default configuration can be overridden.
See the `pom.xml` for all possible configuration options.

Example: To change the baseUrl use this command:

    mvn verify -DbaseUrl=http://localhost:2990/jira/
