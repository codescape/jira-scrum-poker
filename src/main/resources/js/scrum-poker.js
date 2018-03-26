var scrumPokerRefresh;
var scrumPokerRedirect;
var scrumPokerRedirectScheduled = false;

/* Calling the automatic refresh of the Scrum Poker session every 2 seconds. */
function refreshOnInterval(issueKey) {
    refreshSession(issueKey);
    scrumPokerRefresh = setTimeout(function() {
        refreshOnInterval(issueKey);
    }, 2000);
}

/* Refresh the Scrum Poker session and display results using Mustache template */
function refreshSession(issueKey) {
    jQuery.get(AJS.contextPath() + '/rest/scrumpoker/1.0/session/' + issueKey, function(data) {
        jQuery('#card-section').html(
            Mustache.render(jQuery('#card-section-template').html(), data)
        );
        if(typeof data.confirmedVote !== 'undefined') {
            scheduleRedirectToIssue(issueKey);
        } else {
            cancelRedirectToIssue();
        }
    }).fail(function() {
        scheduleRedirectToIssue(issueKey);
    });
}

/* Schedule a redirect to the issue page after 60 seconds */
function scheduleRedirectToIssue(issueKey) {
    if (!scrumPokerRedirectScheduled) {
        scrumPokerRedirect = setTimeout(function() {
            clearTimeout(scrumPokerRefresh);
            window.location.href = AJS.contextPath() + '/browse/' + issueKey;
        }, 60000);
        scrumPokerRedirectScheduled = true;
    }
}

/* Cancel the redirect to the issue page */
function cancelRedirectToIssue() {
    if (scrumPokerRedirectScheduled) {
        clearTimeout(scrumPokerRedirect);
        scrumPokerRedirectScheduled = false;
    }
}

/* Update Scrum Poker session and add or change a vote for the current user */
function updateSession(issueKey, chosenCard) {
    jQuery.post(AJS.contextPath() + '/rest/scrumpoker/1.0/session/' + issueKey + '/card/' + encodeURIComponent(chosenCard), function(data, status) {
        refreshSession(issueKey);
    });
}

/* Reveal all votes giving by participants of this Scrum Poker session */
function revealSession(issueKey) {
    jQuery.post(AJS.contextPath() + '/rest/scrumpoker/1.0/session/' + issueKey + '/reveal', function(data, status) {
        refreshSession(issueKey);
    });
}

/* Reset the Scrum Poker session allowing for a new round for the current issue */
function resetSession(issueKey) {
    jQuery.post(AJS.contextPath() + '/rest/scrumpoker/1.0/session/' + issueKey + '/reset', function(data, status) {
        refreshSession(issueKey);
    });
}

/* Confirm the estimation for a Scrum Poker session ending itself and persisting the estimation on the issue */
function confirmSession(issueKey, estimation) {
    jQuery.post(AJS.contextPath() + '/rest/scrumpoker/1.0/session/' + issueKey + '/confirm/' + encodeURIComponent(estimation), function(data, status) {
        refreshSession(issueKey);
    });
}
