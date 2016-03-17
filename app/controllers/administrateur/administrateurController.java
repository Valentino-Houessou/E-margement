package controllers.administrateur;


import com.fasterxml.jackson.databind.JsonNode;
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

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
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
     * adminIndex()
     * Redirection vers la page d'accueil de l'administrateur
     * @return indexAdministrateur.scala.html
     */
    public Result adminIndex()
    {
        return ok(indexAdministrateur.render("Administration"));
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

        Administrateur.update(idadmin, nom, prenom, adresseMail,  mdp, datenaissance, lienPhoto,  status);

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

        // Chargement des parametres pour affichage dans la vue
        paramPC.remiseAzero();
        paramPC.setLenseignant(enseignant);

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

        return ok(gererUtilisateurEnseignant.render("Gérer les enseignants", lesEnseignants, etape, paramPC));
    }

    /**
     * gererUtilisateurEtudiant()
     * Affichage du bloc dynamique JQuery pour gérer un profil etudiant
     * @return gererUtilisateurEtudiant.scala.html
     */
    public Result gererUtilisateurEtudiant() {
        return ok(gererUtilisateurEtudiant.render("Gérer un profil etudiant"));
    }

    /**
     * chargerListeEtudiant()
     * Affichage du bloc dynamique JQuery pour charger la liste des étudiants
     * @return chargerListeEtudiant.scala.html
     */
    public  Result chargerListeEtudiant()
    {
        return ok(chargerListeEtudiant.render("Charger la liste des étudiants"));
    }

    /**
     * chargerEdt()
     * Affichage du bloc dynamique JQuery pour charger les emplois du temps
     * @return
     */
    public Result chargerEdt()
    {
        return ok(chargerEdt.render("Charger les emplois du temps"));
    }

    /**
     * chargerListeEnseignant()
     * Affichage du bloc dynamique JQuery pour charger la liste des enseignants
     * @return block chargerListeEnseignant.scala.html
     */
    public  Result chargerListeEnseignant()
    {
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

    public  Result exporterFeuilleDatePDF() {
        return pdfGenerator.ok(exporterFeuilleDatePDF.render("By Ftgotc", paramEFP), Configuration.root().getString("application.host"));
    }

    /**
     * gestionAbscencesView()
     * Affichage du bloc dynamique JQuery pour valider les justificatifs d'abscences
     * @return
     */
    public Result gestionAbscences() {
        return ok(gererAbscences.render("Gérer les justificatifs d'abscences"));
    }

    /**
     * exporterJustificatifsAbscences()
     * Affichage du bloc dynamique JQuery pour exporter les justificatifs d'abscences
     * @return
     */
    public Result exporterJustificatifsAbscences() {
        return ok(exporterJustificatifsAbscences.render("Exporter les justificatifs d'abscences"));
    }
}
