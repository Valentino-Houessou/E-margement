package controllers.enseignant;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.*;
import models.Cours;
import models.Enseignant;
import models.Presence;
import models.Utilisateur;
import play.Application;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.enseignant.indexEnseignant;
import views.html.enseignant.list_cours;
import views.html.enseignant.list_etudiants;

import java.util.List;


public class EnseignantController extends Controller{

    private Enseignant teacher;

    public Result index() {
        if(session().get("user_id") == null){
            return redirect(controllers.routes.Application.logout());
        }
        teacher = Enseignant.findByUser(Long.parseLong(session().get("user_id")));
        Form<DateForm> dateform = Form.form(DateForm.class);
        return ok(indexEnseignant.render("Espace Enseignant", teacher, dateform));
    }

    public Result listCours(){
        Form<DateForm> dateform = Form.form(DateForm.class).bindFromRequest();
        return ok(list_cours.render(Cours.findByEnseignant(teacher.id,dateform.get().date)));
    }

    public static class DateForm{
        public String date;

        public String validate(){
            return null;
        }
    }

    public Result listPresence() {
        Form<CoursForm> coursform = Form.form(CoursForm.class).bindFromRequest();
        if(coursform.get().cours.equals("-1"))
            return ok();
        Cours cours = Cours.findbyId(Long.parseLong(coursform.get().cours));
        if(cours.signatureEnseignant)
            return ok("<div class='form-group signature'><span>La feuille de présence de ce cours a été signé.</span></div>");
        return ok(list_etudiants.render(Presence.findbyCours(Long.parseLong(coursform.get().cours))));
    }

    public Result signature(){
        Form<CoursForm> coursform = Form.form(CoursForm.class).bindFromRequest();
        Cours cours = Cours.findbyId(Long.parseLong(coursform.get().cours));
        cours.signatureEnseignant = true;
        cours.update();
        return ok("<div class='form-group signature'><span>La feuille de présence de ce cours a été signé.</span></div>");
    }

    public static class CoursForm {
        public String cours;

        public String validate(){
            return null;
        }
    }

    public Result majPresence(){
        Form<PresenceForm> presenceform = Form.form(PresenceForm.class).bindFromRequest();
        Presence newPresence = Presence.findbyEtudiant(Long.parseLong(presenceform.get().idEtu),Long.parseLong(presenceform.get().cours));
        newPresence.emergement = Boolean.parseBoolean(presenceform.get().presence);
        newPresence.update();
        return ok();
    }

    public static class PresenceForm{
        public String presence;
        public String cours;
        public String idEtu;

        public String validate(){
            return null;
        }
    }

    public  Result getListEnseignant() {
        List<Enseignant> enseignantList;
        enseignantList = Enseignant.findAll();
        return ok(Json.toJson(enseignantList));
    }

    public  Result getEnseignant(long id) {
            return ok(Json.toJson(Enseignant.findById(id)));
    }

}
