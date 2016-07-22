function poll(issueKey) {

    jQuery.ajax({
        type: 'POST',
        url: "/jira/secure/scrumPokerStart.jspa",
        data: "issueKey=" + issueKey + "&action=update",
        success: function(data) {
            jQuery('#card-section').html(data);
        },
        error: function(data){ }
        , complete: setTimeout(function() {
        	poll(issueKey)
        }, 2000),
        timeout: 2000
    });

}
