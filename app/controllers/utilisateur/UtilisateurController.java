package controllers.utilisateur;

import com.fasterxml.jackson.databind.JsonNode;
import models.Utilisateur;
import play.*;
import play.mvc.*;
import com.google.gson.Gson;
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
        Timestamp ddn = null;
        //convertir ddn en Timestamp
        if(ddns != null)
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date ddnd = formatter.parse(ddns);
                 ddn = new Timestamp(ddnd.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        String lPhoto = json.findPath("lPhoto").textValue();

        if((nom != null) && (prenom != null) && (mail != null) && (mdp != null)){
            return ok(Json.toJson(Utilisateur.create(nom, prenom, mail, mdp, ddn,lPhoto, null)));
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
