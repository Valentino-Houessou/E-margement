package controllers;

import models.Utilisateur;
import play.api.Play;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.directionModule;
import views.html.index;

import java.io.File;

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

        System.out.println(session().toString());

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

    public Result logout(){
        session().clear();
        return redirect(routes.Application.index());
    }
}
