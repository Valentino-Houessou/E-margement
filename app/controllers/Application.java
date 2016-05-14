package controllers;

import models.Utilisateur;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.directionModule;
import views.html.index;

import java.util.HashMap;

public class Application extends Controller {

    static Utilisateur user;

    public Result index() {
        return ok(index.render(""));
    }

    public Result login(){
        Form<LoginForm> loginform = Form.form(LoginForm.class).bindFromRequest();

        if(loginform.hasErrors()){
            session().clear();
            return badRequest(index.render(loginform.globalError().message()));
        }

        session().clear();
        session("user_id",String.valueOf(user.id));
        if(user.status().contains("ADMINISTRATEURS"))
            session("administrateur","true");
        else
            session("administrateur","false");
        if(user.status().contains("ETUDIANTS"))
            session("etudiant","true");
        else
            session("etudiant","false");
        if(user.status().contains("ENSEIGNANTS"))
            session("enseignant","true");
        else
            session("enseignant","false");
        return ok(directionModule.render(session()));
    }

    public static class LoginForm{
        public String email;
        public String password;

        public String validate(){
            if((user = Utilisateur.authenticate(email,password)) == null){
                return "Mauvais mot de passe ou Utilisateur non reconnu.";
            }
            return null;
        }
    }

    public Result redirectionModule(){
        if(session().get("user_id") == null)
            return redirect(controllers.routes.Application.logout());
        return ok(directionModule.render(session()));
    }

    public Result logout(){
        session().clear();
        return redirect(routes.Application.index());
    }

    public Result androidLogin(){
        /* Creation - Récupération du Formulaire */
        Form<LoginForm> loginform = Form.form(LoginForm.class).bindFromRequest();
        /* Retour si erreur */
        if(loginform.hasErrors()){
            HashMap<String,String> map = new HashMap<>();
            map.put("error",loginform.globalError().message());
            return badRequest(Json.toJson(map));
        }
        return ok(Json.toJson(user));
    }
}