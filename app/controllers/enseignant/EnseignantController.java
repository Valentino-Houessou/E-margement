package controllers.enseignant;

import models.Cours;
import models.Enseignant;
import models.Presence;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.enseignant.indexEnseignant;
import views.html.enseignant.list_cours;
import views.html.enseignant.list_etudiants;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
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

    public Result androidListCours(){
        Enseignant user = null;
        List<Cours> coursList = null;
        HashMap<String,String> map = new HashMap<>();
        Form<DateForm> dateform = Form.form(DateForm.class).bindFromRequest();
        try{
            user = Enseignant.findByUser(Long.parseLong(dateform.get().anId));
        }catch (NullPointerException e){
            map.put("error","L'id de l'enseignant n'a pu être récupéré !");
            return badRequest(Json.toJson(map));
        }
        try{
            coursList = Cours.findByEnseignant(user.id,dateform.get().date);
            if(coursList.size() == 0){
                map.put("error","Pas de cours à cette date !");
                return badRequest(Json.toJson(map));
            }
        }catch (NullPointerException e){
            map.put("error","Pas de cours à cette date !");
            return badRequest(Json.toJson(map));
        }
        return ok(Json.toJson(coursList));
    }

    public static class DateForm{
        public String date;
        public String anId;

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
        if(cours.heureDebut.after(Timestamp.from(Instant.now())))
            return ok("<div class='form-group signature'><span>Le cours n'a pas commencé.</span></div>");
        if(Timestamp.from(cours.heureFin.toInstant().plusSeconds(900)).before(Timestamp.from(Instant.now())))
            return ok("<div class='form-group signature'><span>Le cours a eu lieu.</span></div>");
        return ok(list_etudiants.render(Presence.findbyCours(Long.parseLong(coursform.get().cours))));
    }

    public Result androidListPresence() {
        Cours cours = null;
        List<Presence> presenceList = null;
        HashMap<String,String> map = new HashMap<>();
        Form<CoursForm> coursform = Form.form(CoursForm.class).bindFromRequest();
        if(coursform.get().cours.equals("-1")){
            map.put("error","Aucun cours selectionné !");
            return badRequest(Json.toJson(map));
        }
        try {
            cours = Cours.findbyId(Long.parseLong(coursform.get().cours));
            if(cours.signatureEnseignant){
                map.put("error","La feuille de présence de ce cours a été signé.");
                return badRequest(Json.toJson(map));
            }
            if(cours.heureDebut.after(Timestamp.from(Instant.now()))){
                map.put("error","Le cours n'a pas commencé.");
                return badRequest(Json.toJson(map));
            }
            if(Timestamp.from(cours.heureFin.toInstant().plusSeconds(900)).before(Timestamp.from(Instant.now()))){
                map.put("error","Le cours a eu lieu.");
                return badRequest(Json.toJson(map));
            }
        }catch (NullPointerException e){
            map.put("error","Nous n'arrivons pas à récupérer le cours !");
            return badRequest(Json.toJson(map));
        }
        try{
            presenceList = Presence.findbyCours(cours.id);
            if(presenceList.size() == 0){
                map.put("error","La liste de présence n'a pas encore été générée !");
                return badRequest(Json.toJson(map));
            }
        }catch (NullPointerException e){
            map.put("error","Nous n'arrivons pas à récupérer la liste pour ce cours !");
            return badRequest(Json.toJson(map));
        }
        return ok(Json.toJson(presenceList));
    }

    public Result signature(){
        Form<CoursForm> coursform = Form.form(CoursForm.class).bindFromRequest();
        Cours cours = Cours.findbyId(Long.parseLong(coursform.get().cours));
        cours.signatureEnseignant = true;
        cours.update();
        return ok("<div class='form-group signature'><span>La feuille de présence de ce cours a été signé.</span></div>");
    }

    public Result androidSignature(){
        HashMap<String,String> map = new HashMap<>();
        Cours cours = null;
        try{
            Form<CoursForm> coursform = Form.form(CoursForm.class).bindFromRequest();
            cours = Cours.findbyId(Long.parseLong(coursform.get().cours));
            cours.signatureEnseignant = true;
            cours.update();
            return ok(Json.toJson("La feuille de présence du cours de " + cours.saMatiere.libelle + " du " + cours.heureDebut + " vient d'être signé"));
        }catch (Exception e){
            map.put("error","Une erreur s'est produite");
            return badRequest(Json.toJson(map));
        }
    }

    public static class CoursForm {
        public String cours;

        public String validate(){
            return null;
        }
    }

    public Result majPresence(){
        HashMap<String,String> map = new HashMap<>();
        try {
            Form<PresenceForm> presenceform = Form.form(PresenceForm.class).bindFromRequest();
            Presence newPresence = Presence.findbyEtudiant(Long.parseLong(presenceform.get().idEtu), Long.parseLong(presenceform.get().cours));
            newPresence.emergement = Boolean.parseBoolean(presenceform.get().presence);
            newPresence.update();
        } catch (Exception e){
            e.printStackTrace();
            map.put("error","Une erreur c'est produite");
            return badRequest(Json.toJson(map));
        }
        return ok(Json.toJson("Présence mise à jour"));
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
