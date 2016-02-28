package controllers.administrateur;


import com.fasterxml.jackson.databind.JsonNode;
import controllers.administrateur.gestionDesParametres.PdfGenerator;
import models.Universite;
import play.Configuration;
import play.libs.Json;
import play.data.DynamicForm;
import static play.data.Form.form;
import play.mvc.*;

import views.html.administrateur.indexAdministrateur;
import views.html.administrateur.gererUtilisateurAdministrateur;
import views.html.administrateur.gererUtilisateurEnseignant;
import views.html.administrateur.gererUtilisateurEtudiant;
import views.html.administrateur.chargerListeEtudiant;
import views.html.administrateur.chargerEdt;
import views.html.administrateur.chargerListeEnseignant;
import views.html.administrateur.exportFeuillePresence;
import views.html.administrateur.exporterFeuilleDatePDF;
import views.html.administrateur.validerJustificatifsAbscences;
import views.html.administrateur.exporterJustificatifsAbscences;
import models.*;

import java.text.ParseException;
import java.util.List;
import controllers.administrateur.gestionDesParametres.parametresExportationFeuillesPresence;


import javax.inject.Inject;


public class administrateurController extends Controller {

    // (Singleton)Permet de gérer l'ensemble des parametres pour la fonction exporter feuille de présence
    public parametresExportationFeuillesPresence paramEFP = parametresExportationFeuillesPresence.getInstance();
    @Inject
    public PdfGenerator pdfGenerator;

    /**
     * adminIndex()
     * Redirection vers la page d'accueil de l'administrateur
     * @return indexAdministrateur.scala.html
     */
    public Result adminIndex()
    {
        return ok(indexAdministrateur.render("Administration"));
    }

    /**
     * gererUtilisateurAdministrateur()
     * Affichage du bloc dynamique JQuery pour gérer un profil administrateur
     * @return gererUtilisateurAdministrateur.scala.html
     */
    public Result gererUtilisateurAdministrateur()
    {
        return ok(gererUtilisateurAdministrateur.render("Gérer un profil administrateur"));
    }

    /**
     * gererUtilisateurEnseignant()
     * Affichage du bloc dynamique JQuery pour gérer un profil enseignant
     * @return gererUtilisateurEnseignant.scala.html
     */
    public Result gererUtilisateurEnseignant() {
        return ok(gererUtilisateurEnseignant.render("Gérer un profil enseignant"));
    }

    /**
     * gererUtilisateurEtudiant()
     * Affichage du bloc dynamique JQuery pour gérer un profil etudiant
     * @return gererUtilisateurEtudiant.scala.html
     */
    public Result gererUtilisateurEtudiant() {
        return ok(gererUtilisateurEtudiant.render("Gérer un profil etudiant"));
    }

    /**
     * chargerListeEtudiant()
     * Affichage du bloc dynamique JQuery pour charger la liste des étudiants
     * @return chargerListeEtudiant.scala.html
     */
    public  Result chargerListeEtudiant()
    {
        return ok(chargerListeEtudiant.render("Charger la liste des étudiants"));
    }

    /**
     * chargerEdt()
     * Affichage du bloc dynamique JQuery pour charger les emplois du temps
     * @return
     */
    public Result chargerEdt()
    {
        return ok(chargerEdt.render("Charger les emplois du temps"));
    }

    /**
     * chargerListeEnseignant()
     * Affichage du bloc dynamique JQuery pour charger la liste des enseignants
     * @return block chargerListeEnseignant.scala.html
     */
    public  Result chargerListeEnseignant()
    {
        return ok(chargerListeEnseignant.render("Charger la liste des enseignants"));
    }

    /**
     * exporterFeuille()
     * Methode : GET
     * Affichage du bloc dynamique JQuery pour exporter les feuilles de présences
     * @return block exportFeuillePresence.scala.html
     */
    public Result exporterFeuille()
    {
        // 0 - Remise à zéro
        paramEFP.setSelectionUniversite(0);
        paramEFP.setSelectionBatiment(0);
        paramEFP.setSelectionFiliere("vide");
        paramEFP.setSelectionPromotion(0);
        paramEFP.setSelectionDate("vide");
        paramEFP.setListeBatiments(null);
        paramEFP.setListeFilieres(null);
        paramEFP.setListePromotion(null);

        // 1 - Etape
        paramEFP.setEtape("selectionUniversite");

        // 2 - Récupération de la liste des universités pour affichage
        List<Universite> universites = Universite.getUniversite();
        paramEFP.setListeUniversites(universites);

        return ok(exportFeuillePresence.render("Exporter des feuilles de présences", parametresExportationFeuillesPresence.getInstance()));
    }

    /**
     * exporterFeuilleselectionBatiment()
     * Method : POST
     * Affichage de la liste des batiments lorsque l'université a été sélectionné
     * @return exportFeuillePresence.scala.html
     */
    public Result exporterFeuilleselectionBatiment()
    {
        // 0 - Remise à zéro
        paramEFP.setSelectionBatiment(0);
        paramEFP.setSelectionFiliere("vide");
        paramEFP.setSelectionPromotion(0);
        paramEFP.setSelectionDate("vide");

        // 1 - Etape
        paramEFP.setEtape("selectionBatiment");

        // 2 - Récupération des batiments de l'université selectionnée
        DynamicForm universites = form().bindFromRequest();
        int universite = Integer.parseInt(universites.get("selectionUniversite"));
        paramEFP.setListeBatiments(Batiment.getBatimentByUniversite(universite));

        // 3 - On garde l'université sélectionné
        paramEFP.setSelectionUniversite(universite);

        return ok(exportFeuillePresence.render("Exporter des feuilles de présences", parametresExportationFeuillesPresence.getInstance()));
    }

    /**
     * exporterFeuilleselectionFiliere()
     * Method : POST
     * Affichage de la liste des filieres lorsque le batiment d'une université a été sélectionné
     * @return
     */
    public Result exporterFeuilleselectionFiliere()
    {
        // 0 - Remise à zéro
        paramEFP.setSelectionFiliere("vide");
        paramEFP.setListePromotion(null);
        paramEFP.setSelectionPromotion(0);
        paramEFP.setSelectionDate("vide");

        // 1 - Etape
        paramEFP.setEtape("selectionFiliere");

        // 2 - Récupération des filières d'un batiments d'une université selectionnée
        DynamicForm batiments = form().bindFromRequest();
        int batiment = Integer.parseInt(batiments.get("selectionBatiment"));
        paramEFP.setListeFilieres(Filiere.getFilieresByBatiment(batiment));

        // 3 - On garde le batiment sélectionné
        paramEFP.setSelectionBatiment(batiment);

        // 4 - On garde l'université sélectionné
        int universite = Integer.parseInt(batiments.get("selectionUniversite"));
        paramEFP.setSelectionUniversite(universite);

        return ok(exportFeuillePresence.render("Exporter des feuilles de présences", parametresExportationFeuillesPresence.getInstance()));
    }

    /**
     * exporterFeuilleselectionPromotion()
     * Méthode : POST
     * Affichage de la liste déroulante des promotions lorsque la filière a été sélectionné
     * @return exportFeuillePresence.scala.html
     */
    public Result exporterFeuilleselectionPromotion()
    {
        // 0 - Remise à zero
        paramEFP.setSelectionPromotion(0);
        paramEFP.setSelectionDate("vide");

        // 1 - Etape
        paramEFP.setEtape("selectionPromotion");

        // 2 - Récupération des promotions d'une filière selectionnée
        DynamicForm selectionfiliere = form().bindFromRequest();
        String filiere = selectionfiliere.get("selectionfiliere"); // Récupere libelle de la filiere
        List<Promotion> promotion = Promotion.getPromotionByFiliere(filiere);
        paramEFP.setListePromotion(promotion);

        // 3 - On garde la filiere
        paramEFP.setSelectionFiliere(filiere);

        // 4 - On garde le batiment
        int batiment = Integer.parseInt(selectionfiliere.get("selectionBatiment"));
        paramEFP.setSelectionBatiment(batiment);

        // 5 - On garde l'universite
        int universite = Integer.parseInt(selectionfiliere.get("selectionUniversite"));
        paramEFP.setSelectionUniversite(universite);

        return ok(exportFeuillePresence.render("Exporter des feuilles de présences", parametresExportationFeuillesPresence.getInstance()));
    }

    /**
     * exporterFeuilleselectionDate()
     * Etape : Sélection de la date
     * @return exportFeuillePresence.scala.html
     */
    public Result exporterFeuilleselectionDate()
    {
        // 0 - Remise à zero
        paramEFP.setSelectionDate("vide");

        // 1 - Etape
        paramEFP.setEtape("selectionDate");

        // 2 - Récupération de la promotion sélectionnée
        DynamicForm selectionpromotion = form().bindFromRequest();
        int promotion = Integer.parseInt(selectionpromotion.get("selectionpromotion"));
        paramEFP.setSelectionPromotion(promotion);

        // La filière, le batiment et l'université non pas bouger du singleton !!!

        return ok(exportFeuillePresence.render("Exporter des feuilles de présences", parametresExportationFeuillesPresence.getInstance()));
    }

    /**
     * exporterFeuilleResultatsParDate()
     * Affichage des récapitulatifs de saisie et de la feuille de présence
     * @return exporterFeuilleselectionDate()
     */
    public Result exporterFeuilleResultatsParDate() throws ParseException {
        // 0 remise à zéro
        paramEFP.setCoursDuJourDeLaPromotion(null);
        paramEFP.setLesEtudiants(null);

        // 1 - Etape
        paramEFP.setEtape("selectionDate");

        // 2 - Récupération de la date choisie et des autres paramètres des listes déroulantes
        DynamicForm selectionDate = form().bindFromRequest();
        int selectionpromotion = Integer.parseInt(selectionDate.get("selectionpromotion"));
        String date = selectionDate.get("datechoisie");
        paramEFP.setSelectionDate(date);

        // 3 - On garde la promotion
        paramEFP.setSelectionPromotion(selectionpromotion);

        // 4 - Construction de la feuille de présence
        List<Cours> coursDuJourDeLaPromotion= parametresExportationFeuillesPresence.getCoursDuJourDeLaPromotion(selectionpromotion, date);
        paramEFP.setCoursDuJourDeLaPromotion(coursDuJourDeLaPromotion);
        List<Utilisateur> lesEtudiants = parametresExportationFeuillesPresence.getEtudiantsParPromotion(selectionpromotion);
        paramEFP.setLesEtudiants(lesEtudiants);

        return ok(exportFeuillePresence.render("Exporter des feuilles de présences", parametresExportationFeuillesPresence.getInstance()));
    }

    public  Result exporterFeuilleDatePDF() {
        return pdfGenerator.ok(exporterFeuilleDatePDF.render("Hello world", parametresExportationFeuillesPresence.getInstance()), Configuration.root().getString("application.host"));
    }

    /**
     * validerJustificatifsAbscences()
     * Affichage du bloc dynamique JQuery pour valider les justificatifs d'abscences
     * @return
     */
    public Result validerJustificatifsAbscences() {
        return ok(validerJustificatifsAbscences.render("Valider les justificatifs d'abscences"));
    }

    /**
     * exporterJustificatifsAbscences()
     * Affichage du bloc dynamique JQuery pour exporter les justificatifs d'abscences
     * @return
     */
    public Result exporterJustificatifsAbscences() {
        return ok(exporterJustificatifsAbscences.render("Exporter les justificatifs d'abscences"));
    }





    public Result addAdmin() {
        JsonNode admin = request().body().asJson();
        if (admin == null)
            return badRequest("donnée Json attendu");
        else {
            String nom = admin.findPath("nom").textValue();
            String prenom = admin.findPath("prenom").textValue();
            String adresseMail = admin.findPath("adresseMail").textValue();
            String motDePasse = admin.findPath("motDePasse").textValue();
            String dateDeNaissance = admin.findPath("dateDeNaissance").textValue();
            String lienPhoto = admin.findPath("lienPhoto").textValue();
            String statut = admin.findPath("statut").textValue();

            if (statut == null)
                return badRequest("paramètre [statut] attendu");
            else if (nom == null)
                return badRequest("paramètre [nom] attendu");
            else if (prenom == null)
                return badRequest("paramètre [prenom] attendu");
            else if (adresseMail == null)
                return badRequest("paramètre [adresseMail] attendu");
            else if (motDePasse == null)
                return badRequest("paramètre [motDePasse] attendu");
            else if (dateDeNaissance == null)
                return badRequest("paramètre [dateDeNaissance] attendu");
            else if (lienPhoto == null)
                return badRequest("paramètre [lienPhoto] attendu");
            else {

                Administrateur administrateur = Administrateur.create(nom, prenom, adresseMail, motDePasse, dateDeNaissance, lienPhoto, statut);

                return ok(Json.toJson(administrateur));
            }
        }
    }

    public  Result updateAdmin() {
        JsonNode admin = request().body().asJson();
        if (admin == null)
            return badRequest("donnée Json attendu");
        else {
            int id = admin.findPath("id").intValue();
            String statut = admin.findPath("statut").textValue();
            String nom = admin.findPath("nom").textValue();
            String prenom = admin.findPath("prenom").textValue();
            String adresseMail = admin.findPath("adresseMail").textValue();
            String motDePasse = admin.findPath("motDePasse").textValue();
            String dateDeNaissance = admin.findPath("dateDeNaissance").textValue();
            String lienPhoto = admin.findPath("lienPhoto").textValue();

            if (statut == null)
                return badRequest("paramètre [statut] attendu");
            else if (nom == null)
                return badRequest("paramètre [nom] attendu");
            else if (prenom == null)
                return badRequest("paramètre [prenom] attendu");
            else if (adresseMail == null)
                return badRequest("paramètre [adresseMail] attendu");
            else if (motDePasse == null)
                return badRequest("paramètre [motDePasse] attendu");
            else if (dateDeNaissance == null)
                return badRequest("paramètre [dateDeNaissance] attendu");
            else if (lienPhoto == null)
                return badRequest("paramètre [lienPhoto] attendu");
            else {
                Administrateur administrateur = Administrateur.update(id, nom, prenom, adresseMail, motDePasse, dateDeNaissance, lienPhoto, statut);

                return ok(Json.toJson(administrateur));
            }
        }

    }

    public  Result getListAdmin() {
        List<Administrateur> adminList;
        adminList = Administrateur.findAll();
        return ok(Json.toJson(adminList));
    }

    public  Result deleteAdmin() {
        JsonNode admin = request().body().asJson();
        if (admin == null)
            return badRequest("donnée Json attendu");
        else {
            int id = admin.findPath("id").intValue();
            Administrateur.delete(id);
            return ok();
        }
    }

    public  Result getAdmin(long id) {
        return ok(Json.toJson(Administrateur.findById(id)));
    }



}
