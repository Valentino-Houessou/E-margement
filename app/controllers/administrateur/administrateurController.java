package controllers.administrateur;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.administrateur.gestionDesParametres.PdfGenerator;
import models.Universite;
import play.Configuration;
import play.Play;
import play.libs.Json;
import play.data.DynamicForm;
import static play.data.Form.form;
import play.mvc.*;

import views.html.administrateur.indexAdministrateur;
import views.html.administrateur.gererUtilisateurAdministrateur;
import views.html.administrateur.gererUtilisateurEnseignant;
import views.html.administrateur.gererUtilisateurEtudiant;
import views.html.administrateur.chargerListeEtudiant;
import views.html.administrateur.chargerEdt;
import views.html.administrateur.chargerListeEnseignant;
import views.html.administrateur.exportFeuillePresence;
import views.html.administrateur.exporterFeuilleDatePDF;
import views.html.administrateur.gererAbscences;
import views.html.administrateur.exporterJustificatifsAbscences;
import models.*;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import controllers.administrateur.gestionDesParametres.*;



import javax.inject.Inject;


public class administrateurController extends Controller {

    // (Singleton)Permet de gérer l'ensemble des parametres pour la fonction exporter feuille de présence
    public parametresExportationFeuillesPresence paramEFP = parametresExportationFeuillesPresence.getInstance();
    // (Singleton)Permet de gérer l'ensemble des parametres pour la partie gérer enseignant
    public parametresProfilCree paramPC = parametresProfilCree.getInstance();
    // (Singleton)Permet de gérer l'ensemble des parametres pour la partie gérer administrateur
    public parametresAdmin paramAdmin = parametresAdmin.getInstance();
    // Gestion de la génération de pdf
    @Inject
    public PdfGenerator pdfGenerator;

    /**
     * Ajout des cours dans la base de donnnées à partir d'u fichier ICS
     * @return
     */
    public Result importEDT() {

        //  Récupérer les champs du formulaire

        DynamicForm profil = form().bindFromRequest();

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart fichier = body.getFile("fichierICS");

        int promotion = Integer.parseInt(profil.get("choixFormation"));

        File fichierICS = fichier.getFile();
        FileReader lectureFichierICS = null;

        Logger log = Logger.getLogger(administrateurController.class.getName());

        try {
            lectureFichierICS = new FileReader(fichierICS);
            log.log(Level.INFO, "Succès de l'ouverture du fichier : " + fichierICS);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.log(Level.SEVERE, "Pas de fichier " + fichierICS + " trouvé");
        }

        BufferedReader bufferLectureFichierICS = new BufferedReader(lectureFichierICS);

        String line = null;

        String description = null;
        String formation = null;
        String ec = null;
        String intervenant = null;
        String salle = null;
        String dateSTR = null;
        Date debut = null;
        String nomIntervenant = "";
        String prenomIntervenant = null;
        String[] nomPrenomTab = null;
        long duree= 0;

        List<String> listeMatieres = new ArrayList<String>();
        HashMap<String, Long> matiereDuree = new HashMap<String, Long>();

        Date fin = null;

        final String old_format = "yyyyMMdd'T'HHmmss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(old_format);

        try {
            while ((line = bufferLectureFichierICS.readLine()) != null) {
                while (!line.contains("END:")) {
                    if (line.contains("DTSTART")) {
                        dateSTR = line.substring(line.lastIndexOf(':') + 1);
                        debut = simpleDateFormat.parse(dateSTR);
                    }

                    if (line.contains("DTEND")) {
                        dateSTR = line.substring(line.lastIndexOf(':') + 1);
                        fin = simpleDateFormat.parse(dateSTR);
                    }

                    if (line.contains("DESCRIPTION")) {
                        description = line.substring(line.lastIndexOf("DESCRIPTION"), line.lastIndexOf("Grp") - 1);
                        description = description.substring(description.lastIndexOf(':')+1);

                        formation = line.substring(line.lastIndexOf("Formation"), line.lastIndexOf("Ec") - 1);
                        formation = formation.substring(formation.lastIndexOf(':')+1);

                        ec = line.substring(line.lastIndexOf("Ec"), line.lastIndexOf("Intervenant") - 1);
                        ec = ec.substring(ec.lastIndexOf(':')+1);

                        intervenant = line.substring(line.lastIndexOf("Intervenant"));
                        intervenant = intervenant.substring(intervenant.lastIndexOf(':')+1);

                        nomPrenomTab = intervenant.split(" ");

                        prenomIntervenant = nomPrenomTab[0];

                        for(int i=1; i<nomPrenomTab.length; i++) {
                            if(i==nomPrenomTab.length-1)
                                nomIntervenant += nomPrenomTab[i];
                            else
                                nomIntervenant += nomPrenomTab[i] + " ";
                        }

                        if(!listeMatieres.contains(ec))
                            listeMatieres.add(ec);
                    }

                    if (line.contains("LOCATION")) {
                        salle = line.substring(line.lastIndexOf(" ") + 1);
                    }

                    line = bufferLectureFichierICS.readLine();
                }

                duree = fin.getTime() - debut.getTime();

                if(!matiereDuree.containsKey(ec))
                    matiereDuree.put(ec, new Long(duree));
                else{
                    matiereDuree.replace(ec, matiereDuree.get(ec), matiereDuree.get(ec) + duree);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        for(String s : matiereDuree.keySet()) {
            Date date = new Date(matiereDuree.get(s));
            System.out.println(s + " : " + date);
            DateFormat formatter = new SimpleDateFormat("HH:mm");
            String dateFormatted = formatter.format(date);
            System.out.println(s + " : " + dateFormatted);
        }

        if(!listeMatieres.isEmpty()) {
            List<Matiere> matieresBD = Matiere.showAll();

            for (String s : listeMatieres) {
                Matiere temp = new Matiere(s, s, null, matiereDuree.get(s));
                if (!matieresBD.contains(temp))
                    temp.save();
            }
        }

        try {
            bufferLectureFichierICS.close();
            lectureFichierICS.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Promotion> promotions = Promotion.findAll();
        return ok(chargerEdt.render("Charger les emplois du temps", promotions));
    }


    /**
     * adminIndex()
     * Redirection vers la page d'accueil de l'administrateur
     * @return indexAdministrateur.scala.html
     */
    public Result adminIndex()
    {
        if(session().get("user_id") == null){
            return redirect(controllers.routes.Application.logout());
        }
        Utilisateur utilisateur = Utilisateur.find.where().eq("id",session().get("user_id")).findUnique();
        session("prenom", utilisateur.prenom);
        return ok(indexAdministrateur.render("Administration", session()));
    }

    /**
     * gererUtilisateurAdministrateur()
     * Affichage du bloc dynamique JQuery pour gérer un profil administrateur
     * @return gererUtilisateurAdministrateur.scala.html
     */
    public Result gererUtilisateurAdministrateur()
    {
        // 0 - Etape : accueil
        String etape = "accueil";

        // 1 - Récupération des profils administrateurs
        paramAdmin.setLesAdmin(Administrateur.findAll());
        paramAdmin.setLesEnseingnantAdmin(Enseignant.findAll());

        if(session().get("user_id") == null)
            return redirect(controllers.routes.Application.logout());
        return ok(gererUtilisateurAdministrateur.render("Gérer les administrateurs", etape, paramAdmin));
    }

    /**
     * Redirection vers la partie pour ajouter un administrateur
     * @return
     */
    public Result ajoutAdmin() {

        // 0 - Etape : accueil ajouter
        String etape = "accueil-ajouter";
        paramAdmin.remiseAzero();

        if(session().get("user_id") == null)
            return redirect(controllers.routes.Application.logout());
        return ok(gererUtilisateurAdministrateur.render("Gérer les administrateurs", etape, paramAdmin));
    }

    public Result ajoutAdminCreer() {

        // 0 - Etape : créer
        String etape = "creer-admin";

        // 1 - Récupérer les champs du formulaire
        DynamicForm profil = form().bindFromRequest();
        String nom = profil.get("nom");
        String prenom = profil.get("prenom");
        String adresseMail = profil.get("email");
        String mdp = profil.get("mdp");
        String datenaissance = profil.get("datepicker10");
        String status = profil.get("status");
        String lienPhoto ="";

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart photo = body.getFile("photo");

        Administrateur newAdmin = null;

        if (photo != null) {
            String fileName = photo.getFilename();
            String contentType = photo.getContentType();
            java.io.File file = photo.getFile();

            // Ajout dans le dossier : /public/photos-utilisateurs
            String myUploadPath = Play.application().configuration().getString("myUploadPath");

            fileName = nom + "_" + prenom + "_" + fileName;

            file.renameTo(new File(myUploadPath, fileName)); // Enregistrement de la photo dans le dossier

            // Création du profil enseignant avec photo
            if((datenaissance !=null) && (!datenaissance.equals("")))
            {
                datenaissance= datenaissance.replace("/", "-");
                String[] parts = datenaissance.split("-");
                datenaissance = parts[2]+"-"+parts[1]+"-"+parts[0] + " 00:00:00"; // Formatage de la date de naissance pour enregistrement
            }
            lienPhoto = myUploadPath + fileName;

            newAdmin = Administrateur.create(nom, prenom, adresseMail, mdp, datenaissance, lienPhoto, status);
        }else {

            // Création du profil enseignant sans photo
            if((datenaissance !=null) && (!datenaissance.equals("")))
            {
                datenaissance= datenaissance.replace("/", "-");
                String[] parts = datenaissance.split("-");
                datenaissance = parts[2]+"-"+parts[1]+"-"+parts[0] + " 00:00:00"; // Formatage de la date de naissance pour enregistrement
            }

            newAdmin = Administrateur.create(nom, prenom, adresseMail, mdp, datenaissance, "", status);
        }

        // 2 -  Chargement des parametres pour affichage dans la vue
        paramAdmin.remiseAzero();
        paramAdmin.setProfilAdmin(newAdmin);

        return ok(gererUtilisateurAdministrateur.render("Bienvenue à " +  paramAdmin.getPrenom() + " " + paramAdmin.getNom(), etape, paramAdmin));
    }

    /**
     * Affichage du profil de l'administrateur pour modification
     * @param id
     * @return
     */
    public  Result gererAdminProfil(Long id) {

        // 0 - Etape : gerer
        String etape = "accueil-gerer";

        // 1 - Récupération du profil administrateur
        paramAdmin.remiseAzero();
        paramAdmin.setProfilAdmin(Administrateur.findById(id));

        if(session().get("user_id") == null)
            return redirect(controllers.routes.Application.logout());
        return ok(gererUtilisateurAdministrateur.render("Modifier le profil de "+ paramAdmin.getPrenom() + " " + paramAdmin.getNom(), etape, paramAdmin));
    }

    /**
     * Modification du profil administrateur
     * @return
     */
    public Result modifAdmin() {

        // 0 - Etape : modifier
        String etape = "modifier-admin";

        // 1 - Récupération du formulaire
        DynamicForm profil = form().bindFromRequest();
        String nom = profil.get("nom");
        String prenom = profil.get("prenom");
        String adresseMail = profil.get("email");
        String mdp = profil.get("mdp");
        String datenaissance = profil.get("datenaissance");
        String status = profil.get("status");
        String lienPhoto = "";

        int idadmin = Integer.parseInt(profil.get("idadmin"));

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart photo = body.getFile("photo");

        // 2 - Mise à jour
        if((datenaissance !=null) && (!datenaissance.equals("")))
        {
            datenaissance= datenaissance.replace("/", "-");
            String[] parts = datenaissance.split("-");
            datenaissance = parts[2]+"-"+parts[1]+"-"+parts[0] + " 00:00:00"; // Formatage de la date de naissance pour enregistrement
        }

        if(photo != null)
        {
            String fileName = photo.getFilename();
            String contentType = photo.getContentType();
            java.io.File file = photo.getFile();

            // Ajout dans le dossier : /public/photos-utilisateurs
            String myUploadPath = Play.application().configuration().getString("myUploadPath");

            fileName = nom+"_"+prenom+"_"+fileName;

            file.renameTo(new File(myUploadPath, fileName)); // Enregistrement de la photo dans le dossier

            lienPhoto = myUploadPath+fileName;
        }

        Administrateur.update(idadmin, nom, prenom, adresseMail, mdp, datenaissance, lienPhoto, status);

        // 4 -  Chargement des parametres pour affichage dans la vue
        paramAdmin.remiseAzero();
        Administrateur ladmin = Administrateur.findById(idadmin);
        paramAdmin.setProfilAdmin(ladmin);


        return ok(gererUtilisateurAdministrateur.render("Profil "+ paramAdmin.getPrenom() + " " + paramAdmin.getNom() + " mis à jours", etape, paramAdmin));
    }

    /**
     * Supprimer un administrateur
     * @param id
     * @return
     */
    public Result deleteAdmin(long id)
    {
        // 0 - Etape : supprimer
        String etape = "supprimer-admonistrateur";


        // 1 - On garde le nom et prenom pour affichage
        paramAdmin.remiseAzero();
        Administrateur admin = Administrateur.findById(id);
        paramAdmin.setProfilAdmin(admin);

        // 2 - Suppression du profil administrateur
        Administrateur.delete(id);

        // 3 - Récupération des profils administrateurs
        paramAdmin.setLesAdmin(Administrateur.findAll());
        paramAdmin.setLesEnseingnantAdmin(Enseignant.findAll());

        if(session().get("user_id") == null)
            return redirect(controllers.routes.Application.logout());
        return ok(gererUtilisateurAdministrateur.render("Gérer les administrateurs", etape, paramAdmin));
    }

    /**
     * Retirer droit administrateur d'un enseignant
     * @param id
     * @return
     */
    public Result retirerDroit(long id) {

        // 0 - Etape : supprimer
        String etape = "retirer-droit";

        // 1 - Retirer le droit administrateur
        Enseignant enseignant = Enseignant.findById(id);
        Utilisateur.deleteDroitAdmin(enseignant.sonUtilisateur.id);

        // 2 - Récupération des profils administrateurs
        paramAdmin.remiseAzero();
        paramAdmin.setLesAdmin(Administrateur.findAll());
        paramAdmin.setLesEnseingnantAdmin(Enseignant.findAll());

        if(session().get("user_id") == null)
            return redirect(controllers.routes.Application.logout());
        return ok(gererUtilisateurAdministrateur.render("Gérer les administrateurs", etape, paramAdmin));
    }

    /**
     * gererUtilisateurEnseignant()
     * Affichage du bloc dynamique JQuery pour gérer les enseignants
     * @return gererUtilisateurEnseignant.scala.html
     */
    public Result gererUtilisateurEnseignant()
    {
        // 0 - Etape : accueil
        String etape = "accueil";

        // 1 - Récupérer la liste des enseignants
        List<Enseignant> lesEnseignants = Enseignant.findAll();

        if(session().get("user_id") == null)
            return redirect(controllers.routes.Application.logout());
        return ok(gererUtilisateurEnseignant.render("Gérer les enseignants", lesEnseignants, etape, paramPC));
    }

    /**
     * gererUtilisateurEnseignantAjouter()
     * Affiche la page pour ajouter un enseignant dans la base de données
     * @return gererUtilisateurEnseignant.scala.html
     */
    public Result gererUtilisateurEnseignantAjouter()
    {

        // 0 - Etape : ajout
        String etape = "ajout-simple";

        // 1 - Récupérer la liste des enseignants
        List<Enseignant> lesEnseignants = Enseignant.findAll();

        if(session().get("user_id") == null)
            return redirect(controllers.routes.Application.logout());
        return ok(gererUtilisateurEnseignant.render("Gérer les enseignants - Ajout d'un profil", lesEnseignants, etape, paramPC));
    }

    /**
     * gererUtilisateurEnseignantCreer()
     * Création du profil enseignant. Tables [Utilisateur et Enseignants]
     * @return
     */
    public Result gererUtilisateurEnseignantCreer()
    {
        // 0 - Etape : créer
        String etape = "profile-creer";

        // 1 - Récupérer la liste des enseignants
        List<Enseignant> lesEnseignants = Enseignant.findAll();

        // 2 - Récupérer les champs du formulaire
        DynamicForm profil = form().bindFromRequest();
        String nom = profil.get("nom");
        String prenom = profil.get("prenom");
        String adresseMail = profil.get("email");
        String mdp = profil.get("mdp");
        String datenaissance = profil.get("datepicker10");
        String status = profil.get("status");
        String droits = profil.get("droits");

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart photo = body.getFile("photo");

        if (photo != null) {
            String fileName = photo.getFilename();
            String contentType = photo.getContentType();
            java.io.File file = photo.getFile();

            // Ajout dans le dossier : /public/photos-utilisateurs
            String myUploadPath = Play.application().configuration().getString("myUploadPath");

            fileName = nom+"_"+prenom+"_"+fileName;

            file.renameTo(new File(myUploadPath, fileName)); // Enregistrement de la photo dans le dossier

            // Création du profil enseignant avec photo
            if((datenaissance !=null) && (!datenaissance.equals("")))
            {
                datenaissance= datenaissance.replace("/", "-");
                String[] parts = datenaissance.split("-");
                datenaissance = parts[2]+"-"+parts[1]+"-"+parts[0] + " 00:00:00"; // Formatage de la date de naissance pour enregistrement
            }
            String lienPhoto = myUploadPath+fileName;

            Enseignant enseignantCree = Enseignant.create(nom, prenom ,adresseMail, mdp, datenaissance, lienPhoto, status);

            // Liaison avec ses modules
            // Si droits = OUI L'enseignant a les droits d'administrateur
            if(droits.equals("OUI")){
                Utilisateur.droitAdmin(enseignantCree.sonUtilisateur.id);
            }

            // Chargement des paramettres pour affichage dans la vue
            paramPC.remiseAzero();
            paramPC.affectation(nom, prenom, adresseMail, datenaissance, status, droits, lienPhoto);
        } else {

            // Création du profil enseignant sans photo
            if((datenaissance !=null) && (!datenaissance.equals("")))
            {
                datenaissance= datenaissance.replace("/", "-");
                String[] parts = datenaissance.split("-");
                datenaissance = parts[2]+"-"+parts[1]+"-"+parts[0] + " 00:00:00"; // Formatage de la date de naissance pour enregistrement
            }

            Enseignant.create(nom, prenom ,adresseMail, mdp, datenaissance, "", status);

            // Chargement des paramettres pour affichage dans la vue
            paramPC.remiseAzero();
            paramPC.affectation(nom, prenom, adresseMail, datenaissance, status, droits, "");
        }

        return ok(gererUtilisateurEnseignant.render("Bienvenue à " + paramPC.getPrenom() + " " + paramPC.getNom(), lesEnseignants, etape, paramPC));
    }

    /**
     * Partie pour gérer un profil enseignant
     * @param id
     * @return
     */
    public Result getEnseignant(long id)
    {
        // 0 - Etape : gerer
        String etape = "gerer-un-profil-enseignant";

        // 1 - Récupération du profil enseignant à gérer
        Enseignant enseignant = Enseignant.findById(id);

        // 2 - Chargement des parametres pour affichage dans la vue
        paramPC.remiseAzero();
        paramPC.setLenseignant(enseignant);
        paramPC.setEtapeListes("ChoixUniversite");

        List<Cours> lesCoursDuprof = Cours.find.where().eq("son_enseignant_id",enseignant.id).findList();
        paramPC.setLesCoursDuProf(lesCoursDuprof);

        List<Universite> listeUniversite = Universite.getUniversite();
        paramPC.setListeUniversite(listeUniversite);

        if(session().get("user_id") == null)
            return redirect(controllers.routes.Application.logout());
        return ok(gererUtilisateurEnseignant.render("Gérer l'enseignant " + paramPC.getPrenom() + " " + paramPC.getNom(), null, etape, paramPC));

    }

    /**
     * Modification d'un profil d'un professeur
     * @return
     */
    public  Result modifProf() {
        // 0 - Etape : modifier
        String etape = "modifier-un-profil-enseignant";

        // 1 - Récupération du formulaire
        DynamicForm profil = form().bindFromRequest();
        String nom = profil.get("nom");
        String prenom = profil.get("prenom");
        String adresseMail = profil.get("email");
        String mdp = profil.get("mdp");
        String datenaissance = profil.get("datenaissance");
        String status = profil.get("status");
        String droits = profil.get("droits");
        String lienPhoto = "";

        int idenseignant = Integer.parseInt(profil.get("idenseignant"));

        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart photo = body.getFile("photo");

        // 2 - Mise à jour
        if((datenaissance !=null) && (!datenaissance.equals("")))
        {
            datenaissance= datenaissance.replace("/", "-");
            String[] parts = datenaissance.split("-");
            datenaissance = parts[2]+"-"+parts[1]+"-"+parts[0] + " 00:00:00"; // Formatage de la date de naissance pour enregistrement
        }

        if(photo != null)
        {
            String fileName = photo.getFilename();
            String contentType = photo.getContentType();
            java.io.File file = photo.getFile();

            // Ajout dans le dossier : /public/photos-utilisateurs
            String myUploadPath = Play.application().configuration().getString("myUploadPath");

            fileName = nom+"_"+prenom+"_"+fileName;

            file.renameTo(new File(myUploadPath, fileName)); // Enregistrement de la photo dans le dossier

            lienPhoto = myUploadPath+fileName;
        }

        Enseignant.update(idenseignant, nom, prenom, adresseMail, mdp, datenaissance, lienPhoto, status);

        // 3 - Affectation ou suppressiondu droit administrateur
        Enseignant enseignant = Enseignant.findById(idenseignant);
        if((droits.equals("NON")) && (enseignant.sonUtilisateur.sesModules.size() >1))
        {
            // Suppression du droit admin
            Utilisateur.deleteDroitAdmin(enseignant.sonUtilisateur.id);
        }else{
            if((droits.equals("OUI") && (enseignant.sonUtilisateur.sesModules.size() == 1)))
            {
                // Affectation du droit admin
                Utilisateur.droitAdmin(enseignant.sonUtilisateur.id);
            }
        }

        // 4 -  Chargement des parametres pour affichage dans la vue
        paramPC.remiseAzero();
        Enseignant enseignantAjour = Enseignant.findById(idenseignant);
        paramPC.setLenseignant(enseignantAjour);

        List<Cours> lesCoursDuprof = Cours.find.where().eq("son_enseignant_id",enseignant.id ).findList();
        paramPC.setLesCoursDuProf(lesCoursDuprof);

        List<Universite> listeUniversite = Universite.getUniversite();
        paramPC.setListeUniversite(listeUniversite);

        paramPC.setEtapeListes("ChoixUniversite"); // Pour la liste déroulante Université

        return ok(gererUtilisateurEnseignant.render("Gérer l'enseignant " + paramPC.getPrenom() + " " + paramPC.getNom(), null, etape, paramPC));
    }


    /**
     * Affichage des batiments d'une université sélectionné
     * @return
     */
    public Result selectionUniversite() {

        // 0 - Etape : gerer
        String etape = "gerer-un-profil-enseignant";
        paramPC.setListeBatiments(null);
        paramPC.setSelectionBatiment(0);
        paramPC.setListeFilieres(null);
        paramPC.setSelectionFiliere("");
        paramPC.setListePromotion(null);
        paramPC.setSelectionPromotion(0);

        // 1 - Récupération des batiments de l'université selectionnée
        DynamicForm universites = form().bindFromRequest();
        int universite = Integer.parseInt(universites.get("selectionFac"));

        paramPC.setListeBatiments(Batiment.getBatimentByUniversite(universite));

        // 2 - Etape liste
        paramPC.setEtapeListes("selectionBatiment");

        // 3 -  université selectionné
        List<Universite> listeUniversite = Universite.getUniversite();
        paramPC.setListeUniversite(listeUniversite);
        paramPC.setSelectionUniversite(universite);

        return ok(gererUtilisateurEnseignant.render("Gérer l'enseignant " + paramPC.getPrenom() + " " + paramPC.getNom(), null, etape, paramPC));
    }

    public Result selectionBatiment() {

        // 0 - Etape : gerer
        String etape = "gerer-un-profil-enseignant";
        paramPC.setListeFilieres(null);
        paramPC.setSelectionFiliere("");
        paramPC.setListePromotion(null);
        paramPC.setSelectionPromotion(0);
        paramPC.setLesMatieres(null);

        // 1 - Etape liste
        paramPC.setEtapeListes("selectionFiliere");

        // 2 - Récupération des filières d'un batiments d'une université selectionnée
        DynamicForm batiments = form().bindFromRequest();
        int batiment = Integer.parseInt(batiments.get("selectionBatiment"));
        paramPC.setListeFilieres(Filiere.getFilieresByBatiment(batiment));

        // 3 - On garde le batiment sélectionné
        paramPC.setSelectionBatiment(batiment);

        // 4 - On garde l'université sélectionné
        int universite = Integer.parseInt(batiments.get("selectionUniversite"));
        paramPC.setSelectionUniversite(universite);

        return ok(gererUtilisateurEnseignant.render("Gérer l'enseignant " + paramPC.getPrenom() + " " + paramPC.getNom(), null, etape, paramPC));
    }

    public Result selectionPromotion() {

        // 0 - Etape : gerer
        String etape = "gerer-un-profil-enseignant";
        paramPC.setLesMatieres(null);

        // 1 - Etape liste
        paramPC.setEtapeListes("selectionPromotion");

        DynamicForm selectionfiliere = form().bindFromRequest();
        String filiere = selectionfiliere.get("selectionfiliere"); // Récupere libelle de la filiere
        List<Promotion> promotion = Promotion.getPromotionByFiliere(filiere);
        paramPC.setListePromotion(promotion);

        // 2 - On garde la filiere
        paramPC.setSelectionFiliere(filiere);

        return ok(gererUtilisateurEnseignant.render("Gérer l'enseignant " + paramPC.getPrenom() + " " + paramPC.getNom(), null, etape, paramPC));
    }

    public Result affichageCoursPromotion() {

        // 0 - Etape : gerer
        String etape = "gerer-un-profil-enseignant";
        paramPC.setLesCoursDelaMatiereDeLaPromotion(null);
        paramPC.setSelectionMatiere(0);

        // 1 - Etape liste
        paramPC.setEtapeListes("afficheMatiere");

        // 2 - Récupération de l'id de la promotion
        DynamicForm promo = form().bindFromRequest();
        int promotion = Integer.parseInt(promo.get("selectionpromotion"));

        // 3 - Récupération des matières de la promotion choisie
        List<Matiere> lesMatiereDeLaPromotion = Promotion.getMatiereParPromotion(promotion);
        paramPC.setLesMatieres(lesMatiereDeLaPromotion);

        // 4 - On garde la promotion
        paramPC.setSelectionPromotion(promotion);

        return ok(gererUtilisateurEnseignant.render("Gérer l'enseignant " + paramPC.getPrenom() + " " + paramPC.getNom(), null, etape, paramPC));
    }

    public Result afficherLesCoursDeLaMatiere() {

        // 0 - Etape : gerer
        String etape = "gerer-un-profil-enseignant";

        // 1 - Etape liste
        paramPC.setEtapeListes("afficheLesCoursDeLaMatiere");

        // 2 - Récupération de l'id de la promotion
        DynamicForm idmatiere = form().bindFromRequest();
        int idmat = Integer.parseInt(idmatiere.get("idmatiere"));
        int idpromotion = Integer.parseInt(idmatiere.get("idpromotion"));

        // 3 - Récupération des cours par jours de la matière
        List<Cours> lesCours = Cours.findListCoursByIdMatiereIdPromotion(idmat,idpromotion);
        paramPC.setLesCoursDelaMatiereDeLaPromotion(lesCours);
        paramPC.setSelectionMatiere(idmat);

        return ok(gererUtilisateurEnseignant.render("Gérer l'enseignant " + paramPC.getPrenom() + " " + paramPC.getNom(), null, etape, paramPC));
    }

    public Result gererCeCours() {

        // 0 - Etape : gerer
        String etape = "gerer-un-profil-enseignant";

        // 1 - Etape liste
        paramPC.setEtapeListes("afficheLesCoursDeLaMatiere");

        // 2 - Récupération des parametres
        DynamicForm parametres = form().bindFromRequest();
        int idcours = Integer.parseInt(parametres.get("idcours"));
        int idenseignant = Integer.parseInt(parametres.get("idenseignant"));
        int idmatiere = Integer.parseInt(parametres.get("idmatiere"));
        int idpromotion = Integer.parseInt(parametres.get("idpromotion"));
        String action = parametres.get("action");

        // 3 - Affecter le cours sélectionné
        if(action.equals("cocher")){
            Enseignant enseignant = Enseignant.findById(idenseignant);
            Cours.affecterRetirerCours(idcours, enseignant);
        }else{
            if(action.equals("decocher")){
                Cours.affecterRetirerCours(idcours, null);
            }
        }

        // 3 - Récupération des cours par jours de la matière
        List<Cours> lesCours = Cours.findListCoursByIdMatiereIdPromotion(idmatiere, idpromotion);
        paramPC.setLesCoursDelaMatiereDeLaPromotion(lesCours);
        // paramPC.setSelectionMatiere(idmatiere);

        // 4 - Mise à jours des cours du prof
        List<Cours> lesCoursDuprof = Cours.find.where().eq("son_enseignant_id",idenseignant).findList();
        paramPC.setLesCoursDuProf(lesCoursDuprof);

        return ok(gererUtilisateurEnseignant.render("Gérer l'enseignant " + paramPC.getPrenom() + " " + paramPC.getNom(), null, etape, paramPC));
    }

    public Result affecterOuRetirerCours() {

        // 0 - Etape : gerer
        String etape = "gerer-un-profil-enseignant";

        // 1 - Etape liste
        paramPC.setEtapeListes("afficheLesCoursDeLaMatiere");

        // 2 - Récupération des parametres
        DynamicForm parametres = form().bindFromRequest();
        int idenseignant = Integer.parseInt(parametres.get("idprof"));
        int idmatiere = Integer.parseInt(parametres.get("idmat"));
        int idpromo = Integer.parseInt(parametres.get("idpromo"));
        String action = parametres.get("statut");

        if(action.equals("TousAffecter")){
            Enseignant enseignant = Enseignant.findById(idenseignant);
            Cours.affecterTousLesCours(idmatiere, idpromo, enseignant);

        }else{
            if(action.equals("TousRetirer")){
                Enseignant enseignant = Enseignant.findById(idenseignant);
                Cours.retirerTousLesCours(idmatiere, idpromo, enseignant);
            }
        }

        // 3 - Récupération des cours par jours de la matière
        List<Cours> lesCours = Cours.findListCoursByIdMatiereIdPromotion(idmatiere, idpromo);
        paramPC.setLesCoursDelaMatiereDeLaPromotion(lesCours);
        // paramPC.setSelectionMatiere(idmatiere);

        // 4 - Mise à jours des cours du prof
        List<Cours> lesCoursDuprof = Cours.find.where().eq("son_enseignant_id",idenseignant).findList();
        paramPC.setLesCoursDuProf(lesCoursDuprof);

        return ok(gererUtilisateurEnseignant.render("Gérer l'enseignant " + paramPC.getPrenom() + " " + paramPC.getNom(), null, etape, paramPC));
    }

    /**
     * Supprimer un enseignant
     * Suppression trace dans table Enseignant - Utilisateur et Modules
     * @param id
     * @return
     */
    public Result deleteEnseignant(long id) {

        // 0 - Etape : supprimer
        String etape = "supprimer-enseignant";


        // 1 - On garde le nom et prenom pour affichage
        paramPC.remiseAzero();
        Enseignant enseignant = Enseignant.findById(id);
        paramPC.setLenseignant(enseignant);

        // 2 - Suppression du profil enseignant
        Enseignant.delete(id);

        // 3 - Récupérer la liste des enseignants à jours
        List<Enseignant> lesEnseignants = Enseignant.findAll();

        if(session().get("user_id") == null)
            return redirect(controllers.routes.Application.logout());
        return ok(gererUtilisateurEnseignant.render("Gérer les enseignants", lesEnseignants, etape, paramPC));
    }

    /**
     * gererUtilisateurEtudiant()
     * Affichage du bloc dynamique JQuery pour gérer un profil etudiant
     * @return gererUtilisateurEtudiant.scala.html
     */
    public Result gererUtilisateurEtudiant() {

        if(session().get("user_id") == null)
            return redirect(controllers.routes.Application.logout());
        return ok(gererUtilisateurEtudiant.render("Gérer un profil etudiant"));
    }

    /**
     * chargerListeEtudiant()
     * Affichage du bloc dynamique JQuery pour charger la liste des étudiants
     * @return chargerListeEtudiant.scala.html
     */
    public  Result chargerListeEtudiant()
    {
        if(session().get("user_id") == null)
            return redirect(controllers.routes.Application.logout());
        return ok(chargerListeEtudiant.render("Charger la liste des étudiants"));
    }

    /**
     * chargerEdt()
     * Affichage du bloc dynamique JQuery pour charger les emplois du temps
     * @return
     */
    public Result chargerEdt()
    {
        List<Promotion> promotions = Promotion.findAll();
        return ok(chargerEdt.render("Charger les emplois du temps", promotions));
    }

    /**
     * chargerListeEnseignant()
     * Affichage du bloc dynamique JQuery pour charger la liste des enseignants
     * @return block chargerListeEnseignant.scala.html
     */
    public  Result chargerListeEnseignant()
    {
        if(session().get("user_id") == null)
            return redirect(controllers.routes.Application.logout());
        return ok(chargerListeEnseignant.render("Charger la liste des enseignants"));
    }

    /**
     * exporterFeuille()
     * Methode : GET
     * Affichage du bloc dynamique JQuery pour exporter les feuilles de présences
     * @return block exportFeuillePresence.scala.html
     */
    public Result exporterFeuille()
    {
        // 0 - Remise à zéro
        paramEFP.setSelectionUniversite(0);
        paramEFP.setSelectionBatiment(0);
        paramEFP.setSelectionFiliere("vide");
        paramEFP.setSelectionPromotion(0);
        paramEFP.setSelectionDate("vide");
        paramEFP.setListeBatiments(null);
        paramEFP.setListeFilieres(null);
        paramEFP.setListePromotion(null);

        // 1 - Etape
        paramEFP.setEtape("selectionUniversite");

        // 2 - Récupération de la liste des universités pour affichage
        List<Universite> universites = Universite.getUniversite();
        paramEFP.setListeUniversites(universites);

        if(session().get("user_id") == null)
            return redirect(controllers.routes.Application.logout());
        return ok(exportFeuillePresence.render("Exporter des feuilles de présences", paramEFP));
    }

    /**
     * exporterFeuilleselectionBatiment()
     * Method : POST
     * Affichage de la liste des batiments lorsque l'université a été sélectionné
     * @return exportFeuillePresence.scala.html
     */
    public Result exporterFeuilleselectionBatiment()
    {
        // 0 - Remise à zéro
        paramEFP.setSelectionBatiment(0);
        paramEFP.setSelectionFiliere("vide");
        paramEFP.setSelectionPromotion(0);
        paramEFP.setSelectionDate("vide");

        // 1 - Etape
        paramEFP.setEtape("selectionBatiment");

        // 2 - Récupération des batiments de l'université selectionnée
        DynamicForm universites = form().bindFromRequest();
        int universite = Integer.parseInt(universites.get("selectionUniversite"));
        paramEFP.setListeBatiments(Batiment.getBatimentByUniversite(universite));

        // 3 - On garde l'université sélectionné
        paramEFP.setSelectionUniversite(universite);

        return ok(exportFeuillePresence.render("Exporter des feuilles de présences", paramEFP));
    }

    /**
     * exporterFeuilleselectionFiliere()
     * Method : POST
     * Affichage de la liste des filieres lorsque le batiment d'une université a été sélectionné
     * @return
     */
    public Result exporterFeuilleselectionFiliere()
    {
        // 0 - Remise à zéro
        paramEFP.setSelectionFiliere("vide");
        paramEFP.setListePromotion(null);
        paramEFP.setSelectionPromotion(0);
        paramEFP.setSelectionDate("vide");

        // 1 - Etape
        paramEFP.setEtape("selectionFiliere");

        // 2 - Récupération des filières d'un batiments d'une université selectionnée
        DynamicForm batiments = form().bindFromRequest();
        int batiment = Integer.parseInt(batiments.get("selectionBatiment"));
        paramEFP.setListeFilieres(Filiere.getFilieresByBatiment(batiment));

        // 3 - On garde le batiment sélectionné
        paramEFP.setSelectionBatiment(batiment);

        // 4 - On garde l'université sélectionné
        int universite = Integer.parseInt(batiments.get("selectionUniversite"));
        paramEFP.setSelectionUniversite(universite);

        return ok(exportFeuillePresence.render("Exporter des feuilles de présences", paramEFP));
    }

    /**
     * exporterFeuilleselectionPromotion()
     * Méthode : POST
     * Affichage de la liste déroulante des promotions lorsque la filière a été sélectionné
     * @return exportFeuillePresence.scala.html
     */
    public Result exporterFeuilleselectionPromotion()
    {
        // 0 - Remise à zero
        paramEFP.setSelectionPromotion(0);
        paramEFP.setSelectionDate("vide");

        // 1 - Etape
        paramEFP.setEtape("selectionPromotion");

        // 2 - Récupération des promotions d'une filière selectionnée
        DynamicForm selectionfiliere = form().bindFromRequest();
        String filiere = selectionfiliere.get("selectionfiliere"); // Récupere libelle de la filiere
        List<Promotion> promotion = Promotion.getPromotionByFiliere(filiere);
        paramEFP.setListePromotion(promotion);

        // 3 - On garde la filiere
        paramEFP.setSelectionFiliere(filiere);

        // 4 - On garde le batiment
        int batiment = Integer.parseInt(selectionfiliere.get("selectionBatiment"));
        paramEFP.setSelectionBatiment(batiment);

        // 5 - On garde l'universite
        int universite = Integer.parseInt(selectionfiliere.get("selectionUniversite"));
        paramEFP.setSelectionUniversite(universite);

        return ok(exportFeuillePresence.render("Exporter des feuilles de présences", paramEFP));
    }

    /**
     * exporterFeuilleselectionDate()
     * Etape : Sélection de la date
     * @return exportFeuillePresence.scala.html
     */
    public Result exporterFeuilleselectionDate()
    {
        // 0 - Remise à zero
        paramEFP.setSelectionDate("vide");

        // 1 - Etape
        paramEFP.setEtape("selectionDate");

        // 2 - Récupération de la promotion sélectionnée
        DynamicForm selectionpromotion = form().bindFromRequest();
        int promotion = Integer.parseInt(selectionpromotion.get("selectionpromotion"));
        paramEFP.setSelectionPromotion(promotion);

        // La filière, le batiment et l'université non pas bouger du singleton !!!

        return ok(exportFeuillePresence.render("Exporter des feuilles de présences", paramEFP));
    }

    /**
     * exporterFeuilleResultatsParDate()
     * Affichage des récapitulatifs de saisie et de la feuille de présence
     * @return exporterFeuilleselectionDate()
     */
    public Result exporterFeuilleResultatsParDate() throws ParseException {
        // 0 remise à zéro
        paramEFP.setCoursDuJourDeLaPromotion(null);
        paramEFP.setLesEtudiants(null);

        // 1 - Etape
        paramEFP.setEtape("selectionDate");

        // 2 - Récupération de la date choisie et des autres paramètres des listes déroulantes
        DynamicForm selectionDate = form().bindFromRequest();
        int selectionpromotion = Integer.parseInt(selectionDate.get("selectionpromotion"));
        String date = selectionDate.get("datechoisie");
        paramEFP.setSelectionDate(date);

        // 3 - On garde la promotion
        paramEFP.setSelectionPromotion(selectionpromotion);

        // 4 - Construction de la feuille de présence
        List<Cours> coursDuJourDeLaPromotion= parametresExportationFeuillesPresence.getCoursDuJourDeLaPromotion(selectionpromotion, date);
        paramEFP.setCoursDuJourDeLaPromotion(coursDuJourDeLaPromotion);
        List<Utilisateur> lesEtudiants = parametresExportationFeuillesPresence.getEtudiantsParPromotion(selectionpromotion);
        paramEFP.setLesEtudiants(lesEtudiants);

        return ok(exportFeuillePresence.render("Exporter des feuilles de présences", parametresExportationFeuillesPresence.getInstance()));
    }

    /**
     * Exporte la feuille de présence au format PDF
     * @return
     */
    public  Result exporterFeuilleDatePDF() {

        if(session().get("user_id") == null)
            return redirect(controllers.routes.Application.logout());
        return pdfGenerator.ok(exporterFeuilleDatePDF.render("By Ftgotc", paramEFP), Configuration.root().getString("application.host"));
    }

    /**
     * gestionAbscencesView()
     * Affichage du bloc dynamique JQuery pour valider les justificatifs d'abscences
     * @return
     */
    public Result gestionAbscences() {
        if(session().get("user_id") == null)
            return redirect(controllers.routes.Application.logout());
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        List<Promotion> lesPromos = Promotion.getPromotionByYear(year);
        return ok(gererAbscences.render("Gérer les justificatifs d'abscences", lesPromos));
    }

    /**
     * getTheAbscences()
     * Permet de recupérer la liste des absences d'une promotion et d'une date donnée
     * @return : Une liste d'information sur les absences
     */
    @BodyParser.Of(BodyParser.Json.class)
    public Result getTheAbscences() {
        //Récupère les paramètres de la requête au format Json
        JsonNode json = request().body().asJson();
        //recupère le paramètre laPromo qui contient id de la promotion
        int laPromo = json.findPath("promotion").intValue();
        //recupère le paramètre laDate qui contient la date choisiz
        String laDate = json.findPath("laDate").textValue();
        //Variable permettant les formatages des différentes dates
        SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat slotFormat = new SimpleDateFormat("hh:mm");
        Date date = new Date();
        String resultatParse =  null;
        //Variable permettant de formatter la date en résultat json
        ArrayNode result = new ArrayNode(JsonNodeFactory.instance);
        ObjectNode child;
        //test sur les variables de la requête
        if((laPromo != 0) && !(laDate.isEmpty()))
            try {
                date = originalFormat.parse(laDate);
                resultatParse = targetFormat.format(date);
                //Récupération des absences suivant les paramètres de fournie par le front
                List<Presence> absences = Presence.getAbsences(laPromo, resultatParse);
                //Formation du resultat Json
                for(Presence p : absences){
                    child =  Json.newObject();
                    child.put("id", p.id);
                    child.put("heureDebut", slotFormat.format((p.sonCours.heureDebut != null) ? p.sonCours.heureDebut : ""));
                    child.put("heureFin", slotFormat.format(p.sonCours.heureFin != null ? p.sonCours.heureFin : ""));
                    child.put("libelle", p.sonCours.saMatiere.libelle != null ? p.sonCours.saMatiere.libelle : "");
                    child.put("libelleAbregee", p.sonCours.saMatiere.libelleAbregee != null ? p.sonCours.saMatiere.libelleAbregee : "");
                    child.put("numeroEtudiant", p.sonEtudiant.numeroEtudiant != null ? p.sonEtudiant.numeroEtudiant : "");
                    child.put("nomEtudiant", p.sonEtudiant.sonUtilisateur.nom != null ? p.sonEtudiant.sonUtilisateur.nom : "");
                    child.put("prenomEtudiant", p.sonEtudiant.sonUtilisateur.prenom != null ? p.sonEtudiant.sonUtilisateur.prenom : "");
                    child.put("motif", ((p.motif != null) && (!p.motif.equals(""))) ? p.motif : "Aucun");
                    child.put("justificatif", ((p.justificatif != null) && (!p.justificatif.equals(""))) ? p.justificatif : "#");
                    result.add(child);
                    child = null;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        return ok(result);
    }

    /**
     * fileDownload()
     * Permer de faire le téléchargement des fichiers de justificatifs
     * @return
     */
    public Result fileDownload(int presenceId){
        if(session().get("user_id") == null)
            return redirect(controllers.routes.Application.logout());
        String justificatif = Presence.getJustificatif(presenceId);
        response().setContentType("application/x-download");
        return ok(new File(justificatif));
    }
}
