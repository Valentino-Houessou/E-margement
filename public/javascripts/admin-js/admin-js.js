/**
 * Created by Yoan D on 14/01/2016.
 */
$(document).ready(function() {
    $(".ext").on('click',function(event) {
        var url = $(this).prop('href');
        $("#charger_fonctionnalite").load(url);
        event.preventDefault();
    });
});