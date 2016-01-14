package controllers.utilisateur;

import com.fasterxml.jackson.databind.JsonNode;
import models.Utilisateur;
import play.*;
import play.mvc.*;
import play.libs.Json;

import java.text.*;
import java.sql.*;
import java.util.Date;


public class UtilisateurController extends Controller {

    @BodyParser.Of(BodyParser.Json.class)
    public  Result addUtilisateur()  {
        JsonNode json = request().body().asJson();
        String nom = json.findPath("nom").textValue();
        String prenom = json.findPath("prenom").textValue();
        String mail = json.findPath("mail").textValue();
        String mdp = json.findPath("mdp").textValue();
        String ddns = json.findPath("ddn").textValue();
        String lPhoto = json.findPath("lPhoto").textValue();

        if((nom != null) && (prenom != null) && (mail != null) && (mdp != null)){
            return ok(Json.toJson(Utilisateur.create(nom, prenom, mail, mdp, ddns,lPhoto)));
        }
        else
            return badRequest("Missing parameter");
    }

    public  Result getUtilisateur() {
        return ok(Json.toJson(Utilisateur.findAll()));
    }

    public  Result getUtilisateurByMail() {
        return Results.TODO;
    }

    public  Result updateUtilisateur() {
        return Results.TODO;
    }

    public  Result deleteUtilisateur() {
        return Results.TODO;
    }
}
