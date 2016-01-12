package controllers.enseignant;

import com.fasterxml.jackson.databind.JsonNode;
import models.Enseignant;
import models.Utilisateur;
import play.mvc.*;
import play.api.mvc.Results;
import play.libs.Json;

import views.html.enseignant.indexEnseignant;

import java.util.List;


public class EnseignantController extends Controller{

    private Enseignant user;

    public Result index() {
        return ok(indexEnseignant.render("Your new application is ready.",user));

    }

    public Result ajoutProf() {
        JsonNode prof = request().body().asJson();
        if (prof == null)
            return badRequest("donnée Json attendu");
        else {
            String nom = prof.findPath("nom").textValue();
            String prenom = prof.findPath("prenom").textValue();
            String adresseMail = prof.findPath("adresseMail").textValue();
            String motDePasse = prof.findPath("motDePasse").textValue();
            String dateDeNaissance = prof.findPath("dateDeNaissance").textValue();
            String lienPhoto = prof.findPath("lienPhoto").textValue();
            String statut = "Professeur";

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

                Utilisateur utilisateur = new Utilisateur(nom, prenom, adresseMail, motDePasse, dateDeNaissance, lienPhoto);

                Enseignant enseignant = new Enseignant(statut, utilisateur);

                return ok(Json.toJson(enseignant));
            }
        }
    }

    public Result modifProf() {
        JsonNode updateProf = request().body().asJson();
        if (updateProf == null)
            return badRequest("donnée Json attendu");
        else {
            int id = updateProf.findPath("id").intValue();
            String statut = updateProf.findPath("statut").textValue();
            String nom = updateProf.findPath("nom").textValue();
            String prenom = updateProf.findPath("prenom").textValue();
            String adresseMail = updateProf.findPath("adresseMail").textValue();
            String motDePasse = updateProf.findPath("motDePasse").textValue();
            String dateDeNaissance = updateProf.findPath("dateDeNaissance").textValue();
            String lienPhoto = updateProf.findPath("lienPhoto").textValue();

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
                Utilisateur utilisateur = new Utilisateur(nom, prenom, adresseMail, motDePasse, dateDeNaissance, lienPhoto);

                Enseignant enseignant = Enseignant.update(id, statut, utilisateur);

                return ok(Json.toJson(enseignant));
            }
        }
    }

    public  Result getListEnseignant() {
        List<Enseignant> enseignantList;
        enseignantList = Enseignant.findAll();
        System.out.println(enseignantList.get(0).sonUtilisateur.nom);
        return ok(Json.toJson(enseignantList));
    }

    public  Result supProf() {
        JsonNode supprimerProf = request().body().asJson();
        if (supprimerProf == null)
            return badRequest("donnée Json attendu");
        else {
            int id = supprimerProf.findPath("id").intValue();

            Enseignant.delete(id);
            return ok();
        }
    }

    public  Result listPresent() {
        return ok();
    }

}
