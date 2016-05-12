package models;
//TODO
import javax.persistence.*;
import com.avaje.ebean.*;

import java.util.*;

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

    /**
     * Création d'un profil étudiant
     * @param nom
     * @param prenom
     * @param adresseMail
     * @param motDePasse
     * @param dateDeNaissance
     * @param lienPhoto
     * @param uid
     * @param numeroEtudiant
     * @param Statut
     * @return
     */
    public static Etudiant create(String nom, String prenom, String adresseMail, String motDePasse, String dateDeNaissance, String lienPhoto,
                                  String uid, String numeroEtudiant, String Statut){

        // Vérification que l'étudiant n'existe pas dans la base
        Utilisateur checkEtudiant = Utilisateur.find.where().eq("adresse_mail", adresseMail).findUnique();

        Etudiant etudiant = null;

        if(checkEtudiant != null)
        {
            return null;
        }else{
            Utilisateur user =  Utilisateur.create(nom, prenom, adresseMail,motDePasse, dateDeNaissance, lienPhoto);

            etudiant= new Etudiant(uid, numeroEtudiant, Statut, user);

            user.droitEtudiant(user.id);

            etudiant.save();
        }

        return etudiant;
    }

    public static Etudiant update(int id, String nom,String prenom,String adresseMail,String motDePasse,String dateDeNaissance,String lienPhoto, String statut) {

        Etudiant etudiant = find.where().eq("id", id).findUnique();

        etudiant.statut = statut;
        Utilisateur.updateUtilisateur(etudiant.sonUtilisateur.id, nom, prenom, adresseMail, motDePasse, dateDeNaissance, lienPhoto);

        etudiant.save();

        return etudiant;

    }


    public static void delete(long id) {
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

    /**
     * Retourne tous les étudiants de la base
     * @return
     */
    public static List<Etudiant> findAll() {
        List<Etudiant> lesEtudiant =  find.all();

        // Trie des utilisateurs par ordre croissant par rapport à leur nom de famille
        Collections.sort(lesEtudiant, new Comparator<Etudiant>() {
            @Override
            public int compare(Etudiant tc1, Etudiant tc2) {
                return tc1.sonUtilisateur.nom.compareTo(tc2.sonUtilisateur.nom);
            }
        });

        return lesEtudiant;
    }

    /**
     * Suppression d'un profil étudiant
     * @param id
     */
    public static void supprimer(long id)
    {

        Promotion promotion = Promotion.find.where().eq("sesEtudiants.id", id).findUnique();

        // 3 - Suppression de l'étudiant
        Etudiant letudiant = Etudiant.findById(id);

        // On retire ses modules
        Utilisateur utilisateur = letudiant.sonUtilisateur;

        if(utilisateur != null)
        {
            if(utilisateur.sesModules != null){
                Utilisateur.deleteDroitEtudiant(utilisateur.id);
            }
        }

        // On supprime Etudiant et Utilisateur
        letudiant.sonUtilisateur = null;
        letudiant.update();



        utilisateur.update();
        utilisateur.delete();

        // 1 - Retirer l'étudiant de sa promotion
        if(promotion != null){

            Iterator<Etudiant> itr = promotion.sesEtudiants.iterator();
            while(itr.hasNext()) {
                Etudiant e = itr.next();

                // On retire l'étudiant de sa promotion
                if(e.id == id){

                    // 2- Suppression des nupplets dans la table presence
                    Presence.supprimerPresenceCoursEtudiant(e.id);

                    // On retire l'étudiant de la promotion
                    itr.remove();

                }
            }
            promotion.update();
        }

        Etudiant et = Etudiant.findById(id);
        et.delete();


        System.out.println("On PASSS PAR LAAAAAAAAAAAAAAAA  ");
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
