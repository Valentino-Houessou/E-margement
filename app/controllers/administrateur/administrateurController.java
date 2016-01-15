package controllers.administrateur;


import play.*;
import play.mvc.*;

import views.html.administrateur.indexAdministrateur;
import views.html.administrateur.gererUtilisateur;
import views.html.administrateur.chargerListeEtudiant;
import views.html.administrateur.gererUtilisateur;
import views.html.administrateur.chargerListeEnseignant;
import views.html.administrateur.exportFeuillePresence;
import models.*;

public class administrateurController extends Controller {

    /**
     * adminIndex()
     * Redirection vers la page d'accueil de l'administrateur
     * @return
     */
    public Result adminIndex()
    {
        return ok(indexAdministrateur.render("Administration"));
    }

    /**
     * gererUtilisateur()
     * Affichage du bloc dynamique JQuery pour gerer les utilisateurs
     * @return block gererUtilisateur.scala.html
     */
    public Result gererUtilisateur()
    {
        return ok(gererUtilisateur.render("Gerer les utilisateurs"));
    }

    /**
     * chargerListeEtudiant()
     * Affichage du bloc dynamique JQuery pour charger la liste des étudiants
     * @return chargerListeEtudiant.scala.html
     */
    public Result chargerListeEtudiant()
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
        return ok(views.html.administrateur.chargerEdt.render("Charger les emplois du temps"));
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
     * Affichage du bloc dynamique JQuery pour exporter les feuilles de présences
     * @return block exportFeuillePresence.scala.html
     */
    public  Result exporterFeuille()
    {
        return ok(exportFeuillePresence.render("Exporter des feuilles de présences"));
    }

    public  Result setAdmin() {
        return Results.TODO;
    }

    public  Result updateAdmin() {
        return Results.TODO;
    }

    public  Result deleteAdmin() {
        return Results.TODO;
    }
}
