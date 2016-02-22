/**
 * Created by Yanis CHALI on 22/02/2016.
 */
$(document)
    .ready(
        function() {
            var form = $('#filtre-etu');
            form.submit(function() {
                $.ajax({
                    type : form.attr('method'),
                    url : form.attr('action'),
                    data : form.serialize(),
                    success : function(data) {
                        var result = data;
                        if (result == 1) {
                            $('#error')
                                .html(
                                    "<span style='color:red;'> Veuillez saisir vos identifiants </span>");
                        } else if (result == 3) {
                            $('#error')
                                .html(
                                    "<span style='color:red;'> Couple identifiant/Mot de passe incorrect </span>");
                        } else if (result == 2) {
                            window.location.href = "accueil.jsp";
                        }
                    }
                });
                return false;
            });
        });