package controllers.enseignant;

import com.fasterxml.jackson.databind.JsonNode;
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

import java.util.List;


public class EnseignantController extends Controller{

    private Enseignant user;

    public Result index() {
        /* a remplacer par une session id */
        user = Enseignant.findById(11);
        Form<DateForm> dateform = Form.form(DateForm.class);
        return ok(indexEnseignant.render("Espace Enseignant", user, dateform));
    }

    public Result listCours(){
        Form<DateForm> dateform = Form.form(DateForm.class).bindFromRequest();
        return ok(list_cours.render(Cours.findByEnseignant(user.sonUtilisateur.id,dateform.get().date)));
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
            return ok("<span>La feuille de présence de ce cours a été signé.</span>");
        return ok(list_etudiants.render(Presence.findbyCours(Long.parseLong(coursform.get().cours))));
    }

    public Result signature(){
        Form<CoursForm> coursform = Form.form(CoursForm.class).bindFromRequest();
        Cours cours = Cours.findbyId(Long.parseLong(coursform.get().cours));
        cours.signatureEnseignant = true;
        cours.update();
        return ok("<span>La feuille de présence de ce cours a été signé.</span>");
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
            String statut = prof.findPath("statut").textValue();

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

                Enseignant enseignant = Enseignant.create(nom, prenom, adresseMail, motDePasse, dateDeNaissance, lienPhoto, statut);

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
                Enseignant enseignant = Enseignant.update(id, nom, prenom, adresseMail, motDePasse, dateDeNaissance, lienPhoto, statut);

                return ok(Json.toJson(enseignant));
            }
        }
    }

    public  Result getListEnseignant() {
        List<Enseignant> enseignantList;
        enseignantList = Enseignant.findAll();
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

    public  Result getEnseignant(long id) {
            return ok(Json.toJson(Enseignant.findById(id)));
    }

}
