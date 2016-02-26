package controllers.administrateur;


import com.fasterxml.jackson.databind.JsonNode;
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
import views.html.administrateur.validerJustificatifsAbscences;
import views.html.administrateur.exporterJustificatifsAbscences;
import models.Administrateur;
import models.Promotion;

import java.util.List;


public class administrateurController extends Controller {

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
        String etape = "Accueil";
        String filiere ="";
        List<Promotion> promotion = null;
        return ok(exportFeuillePresence.render("Exporter des feuilles de présences", etape, filiere, promotion));
    }

    /**
     * exporterFeuilleselectionfiliere()
     * Méthode : POST
     * Affichage de la liste déroulante des promotions lorsque la filière a été sélectionné
     * @return exportFeuillePresence.scala.html
     */
    public Result exporterFeuilleselectionPromotion()
    {
        String etape = "selectionPromotion";

        // 1- Récupération des promotions selon la filière selectionnée
        DynamicForm selectionfiliere = form().bindFromRequest();
        String filiere = selectionfiliere.get("selectionfiliere");
        List<Promotion> promotion = Promotion.getPromotionByFiliere(filiere);

        return ok(exportFeuillePresence.render("Exporter des feuilles de présences", etape, filiere, promotion));
    }


    /**
     * gestionAbscencesView()
     * Affichage du bloc dynamique JQuery pour valider les justificatifs d'abscences
     * @return
     */
    public Result gestionAbscences() {
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
