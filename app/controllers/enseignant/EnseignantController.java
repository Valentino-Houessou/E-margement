package controllers.enseignant;

import models.Enseignant;
import models.Utilisateur;
import play.libs.Json;
import play.mvc.*;

import views.html.enseignant.indexEnseignant;

import java.sql.Timestamp;

public class EnseignantController extends Controller{

    private Enseignant user;

    public  Result getListEnseignant() {
        return Results.TODO;
    }

    public  Result ajoutProf() {
        return Results.TODO;
    }

    public  Result modifProf() {
        return Results.TODO;
    }

    public  Result supProf() {
        return Results.TODO;
    }

    public  Result listPresent() {
        return Results.TODO;
    }

    public Result index() {
        return ok(indexEnseignant.render("Your new application is ready.",user));

    }

    public Result ajoutProf() {
        JsonNode prof = request().body().asJson();
        String nom = prof.findPath("nom").intValue;
        String prenom = prof.findPath("prenom").textValue();
        String adresseMail = prof.findPath("adresseMail").textValue();
        String motDePasse = prof.findPath("motDePasse").textValue();
        String dateDeNaissance = prof.findPath("dateDeNaissance").textValue();
        String lienPhoto = prof.findPath("lienPhoto").textValue();
        String statut = "Professeur";

        Utilisateur utilisateur = new Utilisateur(nom, prenom, adresseMail, motDePasse, dateDeNaissance, lienPhoto);

        Enseignant enseignant = new Enseignant(statut, utilisateur);

        return ok(Json.toJson(enseignant));
    }

    public Result modifProf() {
        JsonNode updateProf = request().body().asJson();
        int id = updateProf.findPath("id").intValue();
        String statut = updateProf.findPath("statut").textValue();
        String nom = updateProf.findPath("nom").textValue();
        String prenom = updateProf.findPath("prenom").textValue();
        String adresseMail = updateProf.findPath("adresseMail").textValue();
        String motDePasse = updateProf.findPath("motDePasse").textValue();
        String dateDeNaissance = updateProf.findPath("dateDeNaissance").textValue();
        String lienPhoto = updateProf.findPath("lienPhoto").textValue();

        Utilisateur utilisateur = new Utilisateur(nom, prenom, adresseMail, motDePasse, dateDeNaissance, lienPhoto);

        Enseignant enseignant = Enseignant.update(id, statut, utilisateur);

        return ok(Json.toJson(enseignant));
    }

}
