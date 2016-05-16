/**
 * Created by Yoan D on 14/01/2016.
 */

var chargerListeEtudiant = function() {

    montreAttente();

    $.ajax({
        url: "/administrateur/charger-liste-etudiant",
        dataType : "html",
        type: "GET",
        success: function(data) {
            $("#charger_fonctionnalite").empty().hide();
            $("#charger_fonctionnalite").html(data).fadeIn(900);
            cacheAttente();
        },
        error: function() {
            alert("Error!")
        }
    })
};

var chargerEdt = function() {

    montreAttente();
    $.ajax({
        url: "/administrateur/charger-edt",
        dataType : "html",
        type: "GET",
        success: function(data) {
            $("#charger_fonctionnalite").empty().hide();
            $("#charger_fonctionnalite").html(data).fadeIn(900);
            cacheAttente();
        },
        error: function() {
            alert("Error!")
        }
    })
};

var chargerListeEnseignant = function() {

    montreAttente();
    $.ajax({
        url: "/administrateur/charger-liste-enseignant",
        dataType : "html",
        type: "GET",
        success: function(data) {
            $("#charger_fonctionnalite").empty().hide();
            $("#charger_fonctionnalite").html(data).fadeIn(900);
            cacheAttente();
        },
        error: function() {
            alert("Error!")
        }
    })
};

var exportFeuillePresence = function() {

    montreAttente();

    $.ajax({
        url: "/administrateur/exporter-feuille",
        dataType : "html",
        type: "GET",
        success: function(data) {
            $("#charger_fonctionnalite").empty().hide();
            $("#charger_fonctionnalite").html(data).fadeIn(900);
            cacheAttente();

        },
        error: function() {
            alert("Erreur de chargement de la page !");
        }
    })
};

var gererUtilisateurAdministrateur = function() {

    montreAttente();
    $.ajax({
        url: "/administrateur/gerer-utilisateur-administrateur",
        dataType : "html",
        type: "GET",
        success: function(data) {
            $("#charger_fonctionnalite").empty().hide();
            $("#charger_fonctionnalite").html(data).fadeIn(900);
            cacheAttente();
        },
        error: function() {
            alert("Error!")
        }
    })
};

var gererUtilisateurEnseignant = function() {

    montreAttente();
    $.ajax({
        url: "/administrateur/gerer-utilisateur-enseignant",
        dataType : "html",
        type: "GET",
        success: function(data) {
            $("#charger_fonctionnalite").empty().hide();
            $("#charger_fonctionnalite").html(data).fadeIn(900);
            cacheAttente();
        },
        error: function() {
            alert("Error!")
        }
    })
};

var gererUtilisateurEtudiant = function() {

    montreAttente();
    $.ajax({
        url: "/administrateur/gerer-les-etudiant",
        dataType : "html",
        type: "GET",
        success: function(data) {
            $("#charger_fonctionnalite").empty().hide();
            $("#charger_fonctionnalite").html(data).fadeIn(900);
            cacheAttente();
        },
        error: function() {
            alert("Error!")
        }
    })
};

var gererPromotions = function() {

    montreAttente();
    $.ajax({
        url: "/administrateur/gerer-utilisateur-etudiant",
        dataType : "html",
        type: "GET",
        success: function(data) {
            $("#charger_fonctionnalite").empty().hide();
            $("#charger_fonctionnalite").html(data).fadeIn(900);
            cacheAttente();
        },
        error: function() {
            alert("Error!")
        }
    })
};

var gererAbscences = function() {

    montreAttente();
    $.ajax({
        url: "/administrateur/gestion-abscences",
        dataType : "html",
        type: "GET",
        success: function(data) {
            $("#charger_fonctionnalite").empty().hide();
            $("#charger_fonctionnalite").html(data).fadeIn(900);
            cacheAttente();
        },
        error: function() {
            alert("Error!")
        }
    })
};

var exporterJustificatifsAbscences = function() {

    montreAttente();
    $.ajax({
        url: "/administrateur/exporter-justificatifs-abscences",
        dataType : "html",
        type: "GET",
        success: function(data) {
            $("#charger_fonctionnalite").empty().hide();
            $("#charger_fonctionnalite").html(data).fadeIn(900);
            cacheAttente();
        },
        error: function() {
            alert("Error!")
        }
    })
};

var ajouteruniversite = function() {

    montreAttente();
    $.ajax({
        url: "/administrateur/ajoutUniversite",
        dataType : "html",
        type: "GET",
        success: function(data) {
            $("#charger_fonctionnalite").empty().hide();
            $("#charger_fonctionnalite").html(data).fadeIn(900);
            cacheAttente();
        },
        error: function() {
            alert("Error!")
        }
    })
};

function montreAttente(){
    var element = document.getElementById('attente');
    element.style.display="block";
}

function cacheAttente(){
    var element = document.getElementById('attente');
    element.style.display="none";
}



$('#ft1').click(chargerListeEtudiant);
$('#ft2').click(chargerEdt);
$('#ft3').click(chargerListeEnseignant);
$('#ft4').click(exportFeuillePresence);
$('#ft5').click(gererUtilisateurAdministrateur);
$('#ft6').click(gererUtilisateurEnseignant);
$('#ft7').click(gererPromotions);
$('#ft8').click(gererAbscences);
$('#ft9').click(exporterJustificatifsAbscences);
$('#ft10').click(gererUtilisateurEtudiant);
$('#ft15').click(ajouteruniversite);

