/**
 * Created by Yoan D on 14/01/2016.
 */

var chargerListeEtudiant = function() {

    $.ajax({
        url: "/administrateur/charger-liste-etudiant",
        dataType : "html",
        type: "GET",
        success: function(data) {
            $("#charger_fonctionnalite").empty().hide();
            $("#charger_fonctionnalite").html(data).fadeIn(900);
        },
        error: function() {
            alert("Error!")
        }
    })
};

var chargerEdt = function() {

    $.ajax({
        url: "/administrateur/charger-edt",
        dataType : "html",
        type: "GET",
        success: function(data) {
            $("#charger_fonctionnalite").empty().hide();
            $("#charger_fonctionnalite").html(data).fadeIn(900);
        },
        error: function() {
            alert("Error!")
        }
    })
};

var chargerListeEnseignant = function() {

    $.ajax({
        url: "/administrateur/charger-liste-enseignant",
        dataType : "html",
        type: "GET",
        success: function(data) {
            $("#charger_fonctionnalite").empty().hide();
            $("#charger_fonctionnalite").html(data).fadeIn(900);

        },
        error: function() {
            alert("Error!")
        }
    })
};

var exportFeuillePresence = function() {

    $.ajax({
        url: "/administrateur/exporter-feuille",
        dataType : "html",
        type: "GET",
        success: function(data) {
            $("#charger_fonctionnalite").empty().hide();
            $("#charger_fonctionnalite").html(data).fadeIn(900);

        },
        error: function() {
            alert("Error!")
        }
    })
};

var gererUtilisateurAdministrateur = function() {

    $.ajax({
        url: "/administrateur/gerer-utilisateur-administrateur",
        dataType : "html",
        type: "GET",
        success: function(data) {
            $("#charger_fonctionnalite").empty().hide();
            $("#charger_fonctionnalite").html(data).fadeIn(900);

        },
        error: function() {
            alert("Error!")
        }
    })
};

var gererUtilisateurEnseignant = function() {

    $.ajax({
        url: "/administrateur/gerer-utilisateur-enseignant",
        dataType : "html",
        type: "GET",
        success: function(data) {
            $("#charger_fonctionnalite").empty().hide();
            $("#charger_fonctionnalite").html(data).fadeIn(900);

        },
        error: function() {
            alert("Error!")
        }
    })
};

var gererUtilisateurEtudiant = function() {

    $.ajax({
        url: "/administrateur/gerer-utilisateur-etudiant",
        dataType : "html",
        type: "GET",
        success: function(data) {
            $("#charger_fonctionnalite").empty().hide();
            $("#charger_fonctionnalite").html(data).fadeIn(900);

        },
        error: function() {
            alert("Error!")
        }
    })
};

$('#ft1').click(chargerListeEtudiant);
$('#ft2').click(chargerEdt);
$('#ft3').click(chargerListeEnseignant);
$('#ft4').click(exportFeuillePresence);
$('#ft5').click(gererUtilisateurAdministrateur);
$('#ft6').click(gererUtilisateurEnseignant);
$('#ft7').click(gererUtilisateurEtudiant);
