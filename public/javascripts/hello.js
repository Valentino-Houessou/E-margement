if (window.console) {
  console.log("Welcome to your Play application's JavaScript!");
}

$(document).ready(function () {

  (function ($) {

    $('#filter').keyup(function () {

      var rex = new RegExp($(this).val(), 'i');
      $('.searchable tr').hide();
      $('.searchable tr').filter(function () {
        return rex.test($(this).text());
      }).show();

    })

  }(jQuery));

});