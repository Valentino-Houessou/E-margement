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

    /**
     * Modifier profil étudiant
     * @param id
     * @param nom
     * @param prenom
     * @param adresseMail
     * @param motDePasse
     * @param dateDeNaissance
     * @param lienPhoto
     * @param statut
     * @return
     */
    public static Etudiant update(long id, String nom,String prenom,String adresseMail,String motDePasse,String dateDeNaissance,String lienPhoto, String uid, String numeroEtudiant, String statut) {

        Etudiant etudiant = find.where().eq("id", id).findUnique();

        etudiant.statut = statut;
        etudiant.numeroEtudiant = numeroEtudiant;
        etudiant.uid = uid;

        Utilisateur.updateUtilisateur(etudiant.sonUtilisateur.id, nom, prenom, adresseMail, motDePasse, dateDeNaissance, lienPhoto);

        etudiant.update();
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
     * La fonction retire un étudiant affecté à une promotion
     * @param idetudiant
     */
    public static void retirerEtudiantPromotion(long idetudiant) {

        Promotion promotion = Promotion.find.where().eq("sesEtudiants.id", idetudiant).findUnique();

        if(promotion != null){
            Iterator<Etudiant> itr = promotion.sesEtudiants.iterator();
            while(itr.hasNext()) {
                Etudiant etudiant = itr.next();

                // On retire l'étudiant de sa promotion
                if(etudiant.id == idetudiant){
                    itr.remove();
                }
            }

            promotion.update();

            // Suppression des nupplets dans la table presence
            Presence.supprimerPresenceCoursEtudiant(idetudiant);
        }
    }

    /**
     * Suppression d'un profil étudiant
     * @param idetudiant
     */
    public static void supprimer(long idetudiant)
    {
        // 1 - Suppression de l'étudiant
        Etudiant letudiant = Etudiant.find.where().eq("id", idetudiant).findUnique();

        Ebean.delete(letudiant);

        //On retire ses modules
        Utilisateur utilisateur = letudiant.sonUtilisateur;


        if(utilisateur.sesModules.size() > 0){
            utilisateur.sesModules.remove(Module.findByLibelle("ETUDIANTS"));

                    utilisateur.update();
        }


        utilisateur.delete();


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
