Contributing
============

Your contribution is always welcome! Please make sure to read this document which aims to help you to contribute in a way that makes it easier to bring your changes into this project.

## Help wanted

There are many things you can do to help improving the Scrum Poker add-on.

* [Create issues](https://github.com/codescape/jira-scrum-poker/issues) for new features or improvements
* Provide new translations for the Scrum Poker add-on
* Add test coverage to untested code
* Refactor the code
* ...

## Development

Development of Atlassian Jira plugins is closely bound to the Atlassian Plugin SDK. Setting up your development environment is documented here: [Set up the Atlassian SDK and build a project](https://developer.atlassian.com/docs/getting-started/set-up-the-atlassian-plugin-sdk-and-build-a-project) 

During development you will find the following commands useful:

* `atlas-run` installs this plugin into Jira and starts it on localhost
* `atlas-debug` same as atlas-run, but allows a debugger to attach at port 5005
* `atlas-package` to generate a Jar file of the plugin which can be installed into your Jira instance
* `atlas-clean` to clean up the `target` folder and so allow you to start with a clean Jira instance again

Reloading the plugin during development can be triggered in your web browser when hitting `Shift + Reload` (for example `Shift + Cmd + R` on Mac OS X).

## Commit messages

Commit messages should be written in this format:

    <type>: <subject>
    
    <body>
    
    <footer>

The first line put together from the `type` and the `subject` should not be longer than 70 characters, the second line is always blank and the following lines should be wrapped at 80 characters.

### Allowed `type` values

* feat (new feature)
* fix (bug fix)
* docs (changes to documentation)
* style (formatting, missing semi colons, etc; no code change)
* refactor (refactoring production code)
* test (adding missing tests, refactoring tests; no production code change)
* chore (updating dependencies etc; no production code change)

### Message body

* uses the imperative, present tense: “change” not “changed” nor “changes”
* includes motivation for the change and contrasts with previous behavior

### Message footer

The message footer is used to reference issue that are addressed and closed by this commit. Closed issues should be listed on a separate line in the footer prefixed with "Closes" keyword like this:

    Closes #17

or in case of multiple issues:

    Closes #17, #18, #19

### Sample

    feat: users cannot see sessions for issues without permission
    
    Adds a check to prevent users to see issues they are not allowed to see. Prior
    to this commit users were able to open sessions and see details for issues
    that they are not allowed see in Jira directly.
    
    Closes #17

## Releases

Releasing a new version helps to bring out new features and improvements to our customers. To keep track of all changes we do the following:

* update the [Changelog](changelog.md) with the release date
* update the [POM](pom.xml) file with the new version number
* create a tag for the new version with reference to the latest commit
    ```
    git tag -a <version> -m "<version>" <commit hash>
    git push origin <version>
    ```
* upload and promote the new version at [Atlassian Marketplace](https://marketplace.atlassian.com/manage/plugins/de.codescape.jira.plugins.scrum-poker/versions)

## Credits

* Contributing code by [Elmar Jobs](https://www.ejobs.de) and [Stefan Höhn](https://github.com/stefan-hoehn)
* Inspiration for Contributing page by [Karma](http://karma-runner.github.io)
* Inspiration for Changelog page by [Keep a Changelog](https://keepachangelog.com)
* Intensive testing and feedback by [congstar GmbH](http://www.congstar.de)
