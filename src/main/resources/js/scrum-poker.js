/* Calling the refreshing of the Scrum Poker session every 2 seconds. */
function refreshOnInterval(issueKey) {
    refreshSession(issueKey);
    setTimeout(function() {
        refreshOnInterval(issueKey);
    }, 2000);
}

/* Refresh the Scrum Poker session and display results using Mustache template */
function refreshSession(issueKey) {
    jQuery.get('/jira/rest/scrumpoker/1.0/session/' + issueKey, function(data) {
        jQuery('#card-section').html(
            // TODO Can we use caching capabilities of mustache here?
            Mustache.render(jQuery('#card-section-template').html(), data)
        );
    });
}

/* Update Scrum Poker session and add or change a vote for the current user */
function updateSession(issueKey, chosenCard) {
    jQuery.post('/jira/rest/scrumpoker/1.0/session/' + issueKey + '/card/' + encodeURIComponent(chosenCard), function(data, status) {
        refreshSession(issueKey);
    });
}

/* Reveal all votes giving by participants of this Scrum Poker session */
function revealSession(issueKey) {
    jQuery.post('/jira/rest/scrumpoker/1.0/session/' + issueKey + '/reveal', function(data, status) {
        refreshSession(issueKey);
    });
}

/* Reset the Scrum Poker session allowing for a new round for the current issue */
function resetSession(issueKey) {
    jQuery.post('/jira/rest/scrumpoker/1.0/session/' + issueKey + '/reset', function(data, status) {
        refreshSession(issueKey);
    });
}

/* Confirm the estimation for a Scrum Poker session ending itself and persisting the estimation on the issue */
function confirmSession(issueKey, estimation) {
    jQuery.post('/jira/rest/scrumpoker/1.0/session/' + issueKey + '/confirm/' + encodeURIComponent(estimation), function(data, status) {
        refreshSession(issueKey);
    });
}
