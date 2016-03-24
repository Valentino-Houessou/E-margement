/**
 * Created by Bigval-Mac on 09/03/16.
 */
$( document ).ready(function() {
    $('#myTab a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    });
});