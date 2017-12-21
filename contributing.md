Contributing
============

Your contribution is always welcome! Please make sure to read this document which aims to help you to contribute in a way that makes it easier to bring your changes into this project.

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

The message footer is used to reference issue that are adressed and closed by this commit. Closed issues should be listed on a separate line in the footer prefixed with "Closes" keyword like this:

    Closes #17

or in case of multiple issues:

    Closes #17, #18, #19

### Sample

    feat: users cannot see sessions for issues without permission
    
    Adds a check to prevent users to see issues they are not allowed to see. Prior
    to this commit users were able to open sessions and see details for issues 
    that they are not allowed see in JIRA directly.
    
    Closes #17

## Credits

Created with inspiration from http://karma-runner.github.io/0.10/dev/git-commit-msg.html.
