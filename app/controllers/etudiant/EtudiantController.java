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
import views.html.etudiant.changerMDP;
import views.html.etudiant.consulterAbsences;
import views.html.etudiant.indexEtudiant;
import views.html.etudiant.justifierAbsences;
import models.Presence;
//import org.jboss.vfs.VirtualFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static play.data.Form.form;
import org.apache.commons.mail.*;

public class EtudiantController extends Controller{

    private Etudiant user;

    public Result changerMDPerreur() {
        return ok(changerMDP.render("Changement du mot de passe",0));
    }

    public  Result mdpchange() {

        int idUtilisateur = Integer.parseInt(session().get("user_id"));

        int erreurMdp;
        DynamicForm changemdp = form().bindFromRequest();
        String mdp = changemdp.get("mdp");
        String mdp2 = changemdp.get("mdp2");

        System.out.println(mdp + " " + mdp2);

        if (mdp.equals(mdp2)) {
            erreurMdp = 1;

            Utilisateur utilisateur = Utilisateur.find.where().eq("id",session().get("user_id")).findUnique();

            System.out.println(utilisateur.adresseMail);
            System.out.println(utilisateur.motDePasse);

            utilisateur.setMotDePasse(mdp);
            utilisateur.adresseMail="boulit@gmail.com";

            utilisateur.update();

            utilisateur.save();


            return redirect(routes.EtudiantController.index());
        }
        else {
            System.out.println("je passe par là");
            erreurMdp = 0;
            return redirect(routes.EtudiantController.changerMDPerreur());
        }

    }


    public  Result changerMDP() {

        return ok(changerMDP.render("Changement du mot de passe",1));
    }

    public Result index() {

        if(session().get("user_id") == null){
            return redirect(controllers.routes.Application.logout());
        }

        //sesssion : idEtudiant
        int idUtilisateur = Integer.parseInt(session().get("user_id"));
        Etudiant idEtudiant = Etudiant.find.where().eq("son_utilisateur_id", idUtilisateur).findUnique();

        int nbabsc = Presence.getNombreAbsence(idEtudiant.id);

        Utilisateur utilisateur = Utilisateur.find.where().eq("id",session().get("user_id")).findUnique();

        session("prenom", utilisateur.prenom);
        return ok(indexEtudiant.render("Espace étudiant",nbabsc, session()));
    }

    public  Result justifierAbsences() {
        //sesssion : idEtudiant
        int idUtilisateur = Integer.parseInt(session().get("user_id"));
        Etudiant idEtudiant = Etudiant.find.where().eq("son_utilisateur_id", idUtilisateur).findUnique();

        DynamicForm profil = form().bindFromRequest();

        List<Presence> presences = Presence.getCreaneauxAbsences(idEtudiant.numeroEtudiant); // Obtenir les créneaux absents

        int idpresence = Integer.parseInt(profil.get("idpresence"));

        return ok(justifierAbsences.render("Justifiez vos absences",idpresence, String.valueOf(idUtilisateur)));
    }


    public Result fileUpload() throws EmailException {

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

                String myUploadPath = Play.application().configuration().getString("justificatifsPath");
                String dateToFileNameStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

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

                Utilisateur utilisateur = Utilisateur.find.where().eq("id",session().get("user_id")).findUnique();
                String nom = utilisateur.nom;
                String prenom = utilisateur.prenom;

                MultiPartEmail email = new MultiPartEmail();
                email.setHostName("smtp.gmail.com");
                email.setAuthentication("dadikadri@gmail.com","badboy92");
                email.setSmtpPort(465);
                email.setSSL(true);
                //email.setDebug(true);
                email.setFrom("dadikadri@gmail.com","eMargement");
                email.addTo("dadikadri@gmail.com");
                email.setSubject("Justificatif d'absence" + nom + prenom);
                email.setMsg("Bonjour,\n\n" + prenom + " " + nom + " vient de justifier son absence"+
                "\n\nMotif d'absence : " + p.motif);

                // Create the attachment
                EmailAttachment attachment = new EmailAttachment();
                attachment.setPath(p.justificatif);
                //attachment.setDisposition(EmailAttachment.ATTACHMENT);
                attachment.setDisposition(EmailAttachment.ATTACHMENT);
                attachment.setDescription("Justificatif d'absence");

                if(fichier.getFilename().substring(fichier.getFilename().lastIndexOf(".")+1).equalsIgnoreCase("pdf")) {
                    attachment.setName("justificatif_" + dateToFileNameStr + ".pdf");
                }

                if(fichier.getFilename().substring(fichier.getFilename().lastIndexOf(".")+1).equalsIgnoreCase("png")) {
                    attachment.setName("justificatif_" + dateToFileNameStr + ".png");
                }

                if(fichier.getFilename().substring(fichier.getFilename().lastIndexOf(".")+1).equalsIgnoreCase("jpg")) {
                    attachment.setName("justificatif_" + dateToFileNameStr + ".jpg");
                }

                email.attach(attachment);


                // Envoi du mail
                email.send();



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



        return ok(consulterAbsences.render("Export de la liste des matières", presences));
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

        return ok(indexEtudiant.render("Partie Etudiant", nbabsc, session()));
    }

}





