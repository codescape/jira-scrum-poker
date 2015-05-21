function poll(issueKey) {

    jQuery.ajax({
        type: 'POST',
        url: "/jira/secure/startPlanningPoker.jspa",
        data: "issueKey="+issueKey+"&action=update",
        success: function(data) {
            jQuery('#card-section').html(data);
            //alert("OK"+data);
        },
        error: function(data){
            //alert("error"+data);
        }
        , complete: setTimeout(function() {poll(issueKey)}, 4000)
        ,timeout: 4000
    })
}


var rotation = 1
var step =0;
var deckRevealed = false;

function initFlip() {
    if (deckRevealed==true)
      return;
    rotation = 1;
    step = 0;
    var $chosencards = jQuery('#chosencards');
    transformCards($chosencards,'img.back',1);
    transformCards($chosencards,'img.front',0);

    showHideDecks(".deckDynamic", true);
    showHideDecks(".deckStatic", false);

}

function resetRevealed() {
    deckRevealed = false;
}

function flip() {
    if (deckRevealed==true)
       return;

    var $chosencards = jQuery('#chosencards');

    if (step == 0) {
        transformCards($chosencards,'img.back',rotation);
    } else {
        transformCards($chosencards,'img.front',(1-rotation));
    }
    if (rotation>0) {
        rotation = rotation - 0.05;
        setTimeout(flip,100);
    } else {
        rotation = 1;
        if (step == 0) {
            step = 1;
            setTimeout(flip,100);
        } else {
            step = 0;
            deckRevealed = true;
            showHideDecks(".deckDynamic", false);
            showHideDecks(".deckStatic", true);
        }
    }
}

function showHideDecks(clazz, show) {
    var $chosencards = jQuery('#chosencards');
    var $images = $chosencards.find(clazz);
    jQuery.each($images, function () {
        if (show==true) {
            jQuery(this).show();
        } else {
            jQuery(this).hide();
        }
    });
}

function transform(element, rot) {
    jQuery(element).css('-moz-transform', 'scaleY(' + (rot) + ')');
    jQuery(element).css('-webkit-transform', 'scaleY(' + (rot) + ')');
    jQuery(element).css('-o-transform', 'scaleY(' + (rot) + ')');
    jQuery(element).css('transform', 'scaleY(' + (rot) + ')');
}

function transformCards(cards, selector, rot) {
    var $images = cards.find(selector);
    jQuery.each($images, function () {
        transform(this,rot);
    });
}
