package controllers.utilisateur;

import com.fasterxml.jackson.databind.*;
import models.Utilisateur;
import play.mvc.*;
import play.libs.Json;

public class UtilisateurController extends Controller {

    @BodyParser.Of(BodyParser.Json.class)
    public  Result addUtilisateur()  {
        JsonNode json = request().body().asJson();
        String nom = json.findPath("nom").textValue();
        String prenom = json.findPath("prenom").textValue();
        String mail = json.findPath("mail").textValue();
        String mdp = json.findPath("mdp").textValue();
        String ddn = json.findPath("ddn").textValue();
        String lPhoto = json.findPath("lPhoto").textValue();

        /*if((nom != null) && (prenom != null) && (mail != null) && (mdp != null)){
            return ok(Json.toJson(Utilisateur.create(nom, prenom, mail, mdp, ddn,lPhoto)));
        }
        else*/
            return badRequest("Missing parameter");
    }

    public  Result getUtilisateurs() {
        return ok(Json.toJson(Utilisateur.findAll()));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public  Result getUtilisateurByMail() {
        JsonNode json = request().body().asJson();
        String mail = json.findPath("mail").textValue();
        if(mail != null)
            return ok(Json.toJson(Utilisateur.findByMail(mail)));
        return badRequest("Missing parameter");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public  Result updateUtilisateur() {
        JsonNode json = request().body().asJson();
        long id = json.findPath("id").longValue();
        String nom = json.findPath("nom").textValue();
        String prenom = json.findPath("prenom").textValue();
        String mail = json.findPath("mail").textValue();
        String mdp = json.findPath("mdp").textValue();
        String ddn = json.findPath("ddn").textValue();
        String lPhoto = json.findPath("lPhoto").textValue();

        if((id == -1) || (nom == null) && (prenom == null) && (mail == null) && (mdp == null)
                && (ddn == null) && (lPhoto == null)){
            return badRequest("Missing parameter");
        }
        else
            return ok(Json.toJson(Utilisateur.updateUtilisateur(id, nom, prenom, mail, mdp, ddn, lPhoto)));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public  Result deleteUtilisateur() {
        JsonNode json = request().body().asJson();
        long id = json.findPath("id").longValue();
        if(id == -1)
            return badRequest("Missing parameter");
        Utilisateur.deleteUtilisateur(id);
        return ok("delete");
    }
}
