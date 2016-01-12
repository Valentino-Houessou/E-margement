package controllers.utilisateur;

import com.fasterxml.jackson.databind.JsonNode;
import models.Utilisateur;
import play.*;
import play.mvc.*;
import com.google.gson.Gson;
import play.libs.Json;
import java.util.List;


public class UtilisateurController extends Controller {

    @BodyParser.Of(BodyParser.Json.class)
    public  Result addUtilisateur() {

        return Results.TODO;
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
