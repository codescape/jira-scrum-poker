# Scrum Poker for Jira (Cloud)

## How to deploy?

First we need to locally start ngrok to tunnel our local http server:

    ngrok http 8080

When doing this we get an url to be used when starting our app: 

    mvn spring-boot:run -Dspring-boot.run.arguments="--addon.base-url=https://57f1f8d2290c.ngrok.io"

To install the app into our cloud instance we go to:

    https://codescape.atlassian.net/plugins/servlet/upm?source=side_nav_manage_addons

And install the app from the following url:

    https://57f1f8d2290c.ngrok.io/atlassian-connect.json
