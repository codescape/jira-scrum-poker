Contributing
============

Your contribution is always welcome!
Please read this document to help you with this.

### Creating new issues

Please feel invited to [create issues](https://github.com/codescape/jira-scrum-poker/issues/new/choose) to let us know about your ideas and wishes or any bugs that you have found.
Have a look at the current list of [open issues](https://github.com/codescape/jira-scrum-poker/issues) to avoid duplicates.

### Improving or adding translations

Want Scrum Poker for Jira to be translated into your language?
Please help us translating our app.
For further details see [Supported Languages](docs/supported-languages.md).

### Running app in development mode

Development of Atlassian Jira plugins is closely bound to the Atlassian Plugin SDK.
Setting up your development environment is documented here: 
[Set up the Atlassian SDK and build a project](https://developer.atlassian.com/docs/getting-started/set-up-the-atlassian-plugin-sdk-and-build-a-project) 

During development you will find the following commands useful:

* `atlas-run` installs this plugin into Jira and starts an instance on localhost
* `atlas-package` generate a Jar file of the plugin and installs it into the currently running instance (see `atlas-run`)
* `atlas-clean` cleans up the `target` folder and so allows you to start with a fresh Jira instance

To fasten up development start a first terminal with a running instance of Jira with the command `atlas-run` and trigger deployments of updated versions by the command `atlas-package`.

### Committing changes

Changes to the source code of Scrum Poker for Jira shall be informative and help to understand what has changed and why.
Commit messages should always be written in this format:

    <type>: <subject>
    
    <body>
    
    <footer>

The first line put together from the `type` and the `subject` should not be longer than 70 characters, the second line is always blank and the following lines should be wrapped at 80 characters.

**Allowed `type` values**

* feat (new feature)
* fix (bug fix)
* docs (changes to documentation)
* style (formatting, missing semi colons, etc; no code change)
* refactor (refactoring production code)
* test (adding missing tests, refactoring tests; no production code change)
* chore (updating dependencies etc; no production code change)

**Message body (optional)**

* uses the imperative, present tense: “change” not “changed” nor “changes”
* includes motivation for the change and contrasts with previous behavior

**Message footer (optional)**

The message footer is used to reference issue that are addressed and closed by this commit.
Closed issues should be listed on a separate line in the footer prefixed with "Closes" keyword like this:

    Closes #17

or in case of multiple issues:

    Closes #17, #18, #19

**Samples**

A simple commit message would look like this:

    feat: configuration page reloads on configuration changes

A more complex sample would look like this:

    feat: users cannot see sessions for issues without permission
    
    Adds a check to prevent users to see issues they are not allowed to see. Prior
    to this commit users were able to open sessions and see details for issues
    that they are not allowed see in Jira directly.
    
    Closes #17

### Releasing new versions

Releasing a new version helps to bring out new features and improvements to our customers.
To keep track of all changes we do the following:

1. update the [POM](pom.xml) file with the new version number
1. add the new version and the release date to the [Changelog](docs/changelog.md)
1. update the [Compatibility Matrix](docs/compatibility-matrix.md) with the new version number
1. create a tag for the new version with reference to the latest commit
    ```
    git rev-parse HEAD
    git tag -a <version> -m "<version>" <commit hash>
    git push origin <version>
    ```
1. upload and promote the new version at [Atlassian Marketplace](https://marketplace.atlassian.com/manage/plugins/de.codescape.jira.plugins.scrum-poker/versions)
