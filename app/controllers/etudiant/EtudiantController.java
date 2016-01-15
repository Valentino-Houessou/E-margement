package controllers.etudiant;

import com.fasterxml.jackson.databind.JsonNode;
import models.Etudiant;
import models.Utilisateur;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import views.html.etudiant.indexEtudiant;
//import views.html.Etudiant.indexEtudiant;
//import views.html.etudiant.indexEtudiant;

import java.util.List;


public class EtudiantController extends Controller{

    private Etudiant user;

    public Result index() {
        return ok(indexEtudiant.render("Your application is ready",user));

    }


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


    public  Result getListEtudiant() {
        List<Etudiant> etudiantList;
        etudiantList = Etudiant.findAll();
        return ok(Json.toJson(etudiantList));
    }

    public  Result supEtudiant() {
        JsonNode supprimerEtudiant = request().body().asJson();
        if (supprimerEtudiant == null)
            return badRequest("données Json attendu");
        else {
            int id = supprimerEtudiant.findPath("id").intValue();

            Etudiant.delete(id);
            return ok();
        }
    }

}
