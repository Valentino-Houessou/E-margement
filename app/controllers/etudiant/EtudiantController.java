package controllers.etudiant;

import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.etudiant.consulterAbsences;
import views.html.etudiant.indexEtudiant;
import models.Presence;

import java.util.List;


public class EtudiantController extends Controller{

    private Etudiant user;

    public Result index() {

        int nbabsc=Presence.getNombreAbsence(1);

        return ok(indexEtudiant.render("Espace étudiant",nbabsc));
    }

    public Result consulterAbsences() {
        return ok(consulterAbsences.render("Partie Etudiant"));

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

             Etudiant eleve = Etudiant.create(numeroEtudiant,nom,prenom,adresseMail,motDePasse,dateDeNaissance,lienPhoto,statut);

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

    public Result nbAbsences(){

        int nbabsc=Presence.getNombreAbsence(31019378);

        return ok(indexEtudiant.render("Partie Etudiant",nbabsc));
    }

    public Result filtrer(String date, String heure_debut, String heure_fin, String cours, String etat){
        return null;
    }

    public Result getCours()
    {
        List<Cours> cours = Cours.findAll();

        return ok(consulterAbsences.render("Export de la liste des cours"));
    }
}
