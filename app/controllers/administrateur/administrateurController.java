package controllers.administrateur;


import com.fasterxml.jackson.databind.JsonNode;
import play.*;
import play.libs.Json;
import play.mvc.*;

import views.html.administrateur.indexAdministrateur;
import views.html.administrateur.gererUtilisateur;
import views.html.administrateur.chargerListeEtudiant;
import views.html.administrateur.gererUtilisateur;
import views.html.administrateur.chargerListeEnseignant;
import views.html.administrateur.exportFeuillePresence;
import models.*;

import java.util.List;

public class administrateurController extends Controller {

    /**
     * adminIndex()
     * Redirection vers la page d'accueil de l'administrateur
     * @return
     */
    public Result adminIndex()
    {
        return ok(indexAdministrateur.render("Administration"));
    }

    /**
     * gererUtilisateur()
     * Affichage du bloc dynamique JQuery pour gerer les utilisateurs
     * @return block gererUtilisateur.scala.html
     */
    public Result gererUtilisateur()
    {
        return ok(gererUtilisateur.render("Gerer les utilisateurs"));
    }

    /**
     * chargerListeEtudiant()
     * Affichage du bloc dynamique JQuery pour charger la liste des étudiants
     * @return chargerListeEtudiant.scala.html
     */
    public Result chargerListeEtudiant()
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
        return ok(views.html.administrateur.chargerEdt.render("Charger les emplois du temps"));
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
     * Affichage du bloc dynamique JQuery pour exporter les feuilles de présences
     * @return block exportFeuillePresence.scala.html
     */
    public  Result exporterFeuille()
    {
        return ok(exportFeuillePresence.render("Exporter des feuilles de présences"));
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
