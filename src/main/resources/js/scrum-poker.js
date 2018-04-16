/*!
 * JavaScript implementation for the client side of the Scrum Poker plugin.
 */
(function(ScrumPoker, $, undefined) {

    var uri = AJS.contextPath() + '/rest/scrumpoker/1.0';
    var refreshTimeout;
    var redirectTimeout;
    var redirectTimeoutActive = false;

    /* Initialize the automatic refresh of the Scrum Poker session every 2 seconds. */
    ScrumPoker.poll = function(issueKey) {
        refreshSession(issueKey);
        refreshTimeout = setTimeout(function() {
            ScrumPoker.poll(issueKey);
        }, 2000);
    }

    /* Refresh the Scrum Poker session and display results using Mustache template */
    function refreshSession(issueKey) {
        $.get(uri + '/session/' + issueKey, function(data) {
            $('#card-section').html(
                Mustache.render($('#card-section-template').html(), data)
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
        if (!redirectTimeoutActive) {
            redirectTimeout = setTimeout(function() {
                clearTimeout(refreshTimeout);
                window.location.href = AJS.contextPath() + '/browse/' + issueKey;
            }, 60000);
            redirectTimeoutActive = true;
        }
    }

    /* Cancel the redirect to the issue page */
    function cancelRedirectToIssue() {
        if (redirectTimeoutActive) {
            clearTimeout(redirectTimeout);
            redirectTimeoutActive = false;
        }
    }

    /* Update Scrum Poker session and add or change a vote for the current user */
    ScrumPoker.updateSession = function(issueKey, chosenCard) {
        $.post(uri + '/session/' + issueKey + '/card/' + encodeURIComponent(chosenCard), function(data, status) {
            refreshSession(issueKey);
        });
    }

    /* Reveal all votes giving by participants of this Scrum Poker session */
    ScrumPoker.revealSession = function(issueKey) {
        $.post(uri + '/session/' + issueKey + '/reveal', function(data, status) {
            refreshSession(issueKey);
        });
    }

    /* Reset the Scrum Poker session allowing for a new round for the current issue */
    ScrumPoker.resetSession = function(issueKey) {
        $.post(uri + '/session/' + issueKey + '/reset', function(data, status) {
            refreshSession(issueKey);
        });
    }

    /* Confirm the estimation for a Scrum Poker session ending itself and persisting the estimation on the issue */
    ScrumPoker.confirmSession = function(issueKey, estimation) {
        $.post(uri + '/session/' + issueKey + '/confirm/' + encodeURIComponent(estimation), function(data, status) {
            refreshSession(issueKey);
        });
    }

}(window.ScrumPoker = window.ScrumPoker || {}, jQuery));
