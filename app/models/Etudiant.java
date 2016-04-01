package models;
//TODO
import javax.persistence.*;
import com.avaje.ebean.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Etudiant extends Model{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public long id;
    public String uid;
    public String numeroEtudiant;
    public String statut;
    @OneToOne
    public Utilisateur sonUtilisateur;


    public Etudiant(String uid, String numeroEtudiant, String status, Utilisateur sonUtilisateur) {
        this.uid = uid;
        this.numeroEtudiant = numeroEtudiant;
        this.statut = status;
        this.sonUtilisateur = sonUtilisateur;
    }



    //TODO : Quand on crée un etudiant il faut ajouter le module etudiant dans la liste de module de son utilisateur Vous pouvez utiliser la fonction droitEtudiant() de utilisateur*/
    /*public static Etudiant create(String numeroEtudiant, String  nom, String prenom, String adresseMail, String motDePasse,
                                  String dateDeNaissance, String lienPhoto, String Statut){

        Utilisateur user =  Utilisateur.create(nom, prenom, adresseMail,motDePasse, dateDeNaissance, lienPhoto);

        Etudiant etudiant= new Etudiant(numeroEtudiant,Statut,user);

        user.droitEtudiant(user.id);

        etudiant.save();

        return etudiant;
    }*/

    public static Etudiant update(int id, String nom,String prenom,String adresseMail,String motDePasse,String dateDeNaissance,String lienPhoto, String statut) {

        Etudiant etudiant = find.where().eq("id", id).findUnique();

        etudiant.statut = statut;
        Utilisateur.updateUtilisateur(etudiant.sonUtilisateur.id, nom, prenom, adresseMail, motDePasse, dateDeNaissance, lienPhoto);

        etudiant.save();

        return etudiant;

    }

    public static void delete(int id) {
        Etudiant etudiant = find.where().eq("id", id).findUnique();
        Utilisateur utilisateur = etudiant.sonUtilisateur;
        Ebean.delete(etudiant);
        if (!Administrateur.utilisateurAdmin(utilisateur))
            Ebean.delete(utilisateur);
    }

    public static boolean utilisateurEtudiant(Utilisateur user){
        return ((Etudiant.find.where().eq("sonUtilisateur",user).findUnique()) != null ? true : false);
    }



    //Finder for retrieve data in database
    public static Finder<Long,Etudiant> find = new Finder<Long,Etudiant>(Etudiant.class);

    public static Etudiant findById(long id) {

        return find.ref(id);
    }

    public static List<Etudiant> findAll() {
        return find.all();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Etudiant etudiant = (Etudiant) o;

        if (id != etudiant.id) return false;
        if (uid != null ? !uid.equals(etudiant.uid) : etudiant.uid != null) return false;
        if (numeroEtudiant != null ? !numeroEtudiant.equals(etudiant.numeroEtudiant) : etudiant.numeroEtudiant != null)
            return false;
        if (statut != null ? !statut.equals(etudiant.statut) : etudiant.statut != null) return false;
        return sonUtilisateur != null ? sonUtilisateur.equals(etudiant.sonUtilisateur) : etudiant.sonUtilisateur == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (numeroEtudiant != null ? numeroEtudiant.hashCode() : 0);
        result = 31 * result + (statut != null ? statut.hashCode() : 0);
        result = 31 * result + (sonUtilisateur != null ? sonUtilisateur.hashCode() : 0);
        return result;
    }
}
