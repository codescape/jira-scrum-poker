/*!
 * JavaScript implementation for the client side of the Scrum Poker plugin for the configuration page.
 */
(function(ScrumPokerConfiguration, $, undefined) {

    var displayAdditionalFields;

    /* render the AUI MultiSelect for the given field id */
    function createMultiSelect(id){
        if (AJS.$('#' + id + "-textarea").length == 0){
            return new AJS.MultiSelect({
                element: $("#" + id),
                itemAttrDisplayed: "label"
            });
        }
    }

    AJS.toInit(function(){
        // create a multi select for additional fields
        displayAdditionalFields = createMultiSelect("displayAdditionalFields");

        // hook into the submit event of the form
        $('#scrumPokerConfiguration').submit(function() {
            // transfer the selected fields into the hidden field and keep sort order of fields
            if (displayAdditionalFields && displayAdditionalFields.lozengeGroup) {
                var ids = displayAdditionalFields.lozengeGroup.items.map(function(item) { return item.value; });
                $('#displayAdditionalFieldsIds').val(ids.join(","));
            }
            // continue form submission
            return true;
        });
     });

}(window.ScrumPokerConfiguration = window.ScrumPokerConfiguration || {}, jQuery));
