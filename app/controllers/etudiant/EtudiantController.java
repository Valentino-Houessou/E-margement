package controllers.etudiant;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.*;
import models.*;
import play.Play;
import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.etudiant.consulterAbsences;
import views.html.etudiant.indexEtudiant;
import views.html.etudiant.justifierAbsences;
import models.Presence;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import static play.data.Form.form;


public class EtudiantController extends Controller{

    private Etudiant user;

    public Result index() {

        //sesssion : idEtudiant
        int idUtilisateur = Integer.parseInt(session().get("user_id"));
        if(session().get("user_id") == null){
            return redirect(controllers.routes.Application.logout());
        }

        Etudiant idEtudiant = Etudiant.find.where().eq("son_utilisateur_id", idUtilisateur).findUnique();

        Utilisateur utilisateur = Utilisateur.find.where().eq("id",session().get("user_id")).findUnique();

        session("prenom", utilisateur.prenom);

        int nbabsc = Presence.getNombreAbsence(idEtudiant.id);

        return ok(indexEtudiant.render("Accueil étudiant",nbabsc,session()));
    }

    public  Result justifierAbsences(){
        //sesssion : idEtudiant
        int idUtilisateur = Integer.parseInt(session().get("user_id"));
        Etudiant idEtudiant = Etudiant.find.where().eq("son_utilisateur_id", idUtilisateur).findUnique();

        DynamicForm profil = form().bindFromRequest();

        List<Presence> presences = Presence.getCreaneauxAbsences(idEtudiant.numeroEtudiant); // Obtenir les créneaux absents

        int idpresence = Integer.parseInt(profil.get("idpresence"));

        return ok(justifierAbsences.render("Justifiez vos absences",idpresence,""));
    }


    public Result fileUpload() {

        //  Récupérer les champs du formulaire

        DynamicForm profil = form().bindFromRequest();

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart fichier = body.getFile("fileUpload");

        int idpresence = Integer.parseInt(profil.get("idpresence"));
        //controle sur le motif
        String motif=profil.get("motif");


        if (fichier != null && fichier.getFile().length()<1000000) {

            if(fichier.getFilename().substring(fichier.getFilename().lastIndexOf(".")+1).equalsIgnoreCase("pdf")||fichier.getFilename().substring(fichier.getFilename().lastIndexOf(".")+1).equalsIgnoreCase("png")||fichier.getFilename().substring(fichier.getFilename().lastIndexOf(".")+1).equalsIgnoreCase("jpg")) {
                String fileName = fichier.getFilename();
                String contentType = fichier.getContentType();
                File file = fichier.getFile();

                // Ajout dans le dossier Image : C:\Users\Yoan D\Desktop\Play_Framework_2.0\m2a20152016-feuillepresence\public\images\Photos-utilisateurs
                String myUploadPath = Play.application().configuration().getString("justificatifsPath");
                String dateToFileNameStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

                //file.renameTo(new File(myUploadPath, user.numeroEtudiant + "_" + user.sonUtilisateur.prenom + "_" + user.sonUtilisateur.nom + "_justificatif_" + dateToFileNameStr));
               if(fichier.getFilename().substring(fichier.getFilename().lastIndexOf(".")+1).equalsIgnoreCase("pdf")) {
                   file.renameTo(new File(myUploadPath, "justificatif_" + dateToFileNameStr + ".pdf"));
               }
                if(fichier.getFilename().substring(fichier.getFilename().lastIndexOf(".")+1).equalsIgnoreCase("png")) {
                    file.renameTo(new File(myUploadPath, "justificatif_" + dateToFileNameStr + ".png"));
                }
                if(fichier.getFilename().substring(fichier.getFilename().lastIndexOf(".")+1).equalsIgnoreCase("jpg")) {
                    file.renameTo(new File(myUploadPath, "justificatif_" + dateToFileNameStr + ".jpg"));
                }

                Presence p = Presence.find.where().eq("id", idpresence).findUnique();
                p.motif = motif;
                //p.justificatif= myUploadPath + user.numeroEtudiant + "_" + user.sonUtilisateur.prenom + "_" + user.sonUtilisateur.nom + "_justificatif_" + dateToFileNameStr + ".pdf";
                if(fichier.getFilename().substring(fichier.getFilename().lastIndexOf(".")+1).equalsIgnoreCase("pdf")) {
                    p.justificatif = myUploadPath + "justificatif_" + dateToFileNameStr + ".pdf";
                }

                if(fichier.getFilename().substring(fichier.getFilename().lastIndexOf(".")+1).equalsIgnoreCase("png")) {
                    p.justificatif = myUploadPath + "justificatif_" + dateToFileNameStr + ".png";
                }

                if(fichier.getFilename().substring(fichier.getFilename().lastIndexOf(".")+1).equalsIgnoreCase("jpg")) {
                    p.justificatif = myUploadPath + "justificatif_" + dateToFileNameStr + ".jpg";
                }

                p.save();
            }
            else{
                String erreur="erreurExtension";
                return ok(justifierAbsences.render("Justifiez vos absences",idpresence,erreur));

            }

        } else {
            String erreur="erreurTaille";
            return ok(justifierAbsences.render("Justifiez vos absences",idpresence,erreur));
        }
        /*List<Presence> presences = Presence.getCreaneauxAbsences(3700000);

        return ok(consulterAbsences.render("Consulter vos absences", presences));*/

        return redirect(routes.EtudiantController.consulterAbsences());

    }

    /**
     * Téléchargement du justificatif d'absence
     * @return
     */
    public Result fileDownload(){
        DynamicForm profil = form().bindFromRequest();
        Http.MultipartFormData body = request().body().asMultipartFormData();
        String justificatif = profil.get("justificatifDownload");

        response().setContentType("application/x-download");
        return ok(new File(justificatif));
    }

    public Result consulterAbsences() {
        //sesssion : idEtudiant
        int idUtilisateur = Integer.parseInt(session().get("user_id"));
        Etudiant idEtudiant = Etudiant.find.where().eq("son_utilisateur_id", idUtilisateur).findUnique();

        List<Presence> presences = Presence.getCreaneauxAbsences(idEtudiant.numeroEtudiant);



        return ok(consulterAbsences.render("Consultation d'absences", presences));
    }

    /**
     * Ajoute un nouvel étudiant
     *
     * @returnles informations de l'étudiant créé au format JSON
     */
    public Result ajoutEtudiant() {
        JsonNode etudiant = request().body().asJson();
        if (etudiant == null)
            return badRequest("données Json attendu");
        else {
            String numeroEtudiant=etudiant.findPath("numeroEtudiant").textValue();
            String nom = etudiant.findPath("nom").textValue();
            String prenom = etudiant.findPath("prenom").textValue();
            String adresseMail = etudiant.findPath("adresseMail").textValue();
            String motDePasse = etudiant.findPath("motDePasse").textValue();
            String dateDeNaissance = etudiant.findPath("dateDeNaissance").textValue();
            String lienPhoto = etudiant.findPath("lienPhoto").textValue();
            String statut = "etudiant";

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

                Etudiant eleve = null;//Etudiant.create(numeroEtudiant,nom,prenom,adresseMail,motDePasse,dateDeNaissance,lienPhoto,statut);

                return ok(/*Json.toJson(etudiant)*/);
            }
        }
    }

    /**
     * Permet de modifier les informations d'un étudiant
     *
     * @return les modifications apportées au format JSON
     */
    public Result modifEtudiant() {
        JsonNode updateEtudiant = request().body().asJson();
        if (updateEtudiant == null)
            return badRequest("donnée Json attendu");
        else {
            int id = updateEtudiant.findPath("id").intValue();
            String statut = updateEtudiant.findPath("statut").textValue();
            String nom = updateEtudiant.findPath("nom").textValue();
            String prenom = updateEtudiant.findPath("prenom").textValue();
            String adresseMail = updateEtudiant.findPath("adresseMail").textValue();
            String motDePasse = updateEtudiant.findPath("motDePasse").textValue();
            String dateDeNaissance = updateEtudiant.findPath("dateDeNaissance").textValue();
            String lienPhoto = updateEtudiant.findPath("lienPhoto").textValue();

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
                Etudiant etudiant = Etudiant.update(id, nom, prenom, adresseMail, motDePasse, dateDeNaissance, lienPhoto, statut);

                return ok(/*Json.toJson(etudiant)*/);
            }
        }
    }


    /**
     * Récupère la liste de tous les étudiants
     *
     * @return une liste d'étudiants au format JSON
     */
    public Result getListEtudiant() {
        List<Etudiant> etudiantList;
        etudiantList = Etudiant.findAll();
        return ok(Json.toJson(etudiantList));
    }

    /**
     * Supprime un étudiant
     *
     * @return ok si l'étudiant a bien été supprimé, une erreur sinon
     */
    public Result supEtudiant() {
        JsonNode supprimeretudiant = request().body().asJson();
        if (supprimeretudiant == null)
            return badRequest("données Json attendu");
        else {
            int id = supprimeretudiant.findPath("id").intValue();
            Etudiant.delete(id);
            return ok();
        }
    }


    /**
     * Retourne les absences d'un étudiant
     *
     * @return une liste d'absences au format JSON
     */
    public Result findAbsences() {
        List<Presence> absences = Presence.findAll();
        for (Presence p : absences)
            if (p.sonEtudiant.id != user.id && p.emergement)
                absences.remove(p);
        return ok(Json.toJson(absences));
    }

    /**
     * Récupère le nombre d'absences d'un étudiant
     * @return
     */
    public Result nbAbsences(){

        //sesssion : idEtudiant
        int idUtilisateur = Integer.parseInt(session().get("user_id"));
        Etudiant idEtudiant = Etudiant.find.where().eq("son_utilisateur_id", idUtilisateur).findUnique();

        int nbabsc=Presence.getNombreAbsence(idEtudiant.id);

        return ok(indexEtudiant.render("Accueil Etudiant", nbabsc,session()));
    }
    }





