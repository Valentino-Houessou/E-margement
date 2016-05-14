package models;
//TODO

import javax.persistence.*;
import com.avaje.ebean.*;

import java.util.*;

@Entity
public class Enseignant extends Model{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int id;
    public String statut;
    @OneToOne
    public Utilisateur sonUtilisateur;
    @ManyToMany(cascade=CascadeType.PERSIST)
    public List<Matiere> sesMatieres;

    public static Finder<Long, Enseignant> find = new Finder<Long, Enseignant>(Enseignant.class);

   //TODO : Quand on cré un enseignant il faut ajouter le module Enseignant dans la liste de module de son utilisateur Vous pouvez utiliser la fonction droitEnseignant() de utilisateur*/
    public Enseignant(String statut, Utilisateur sonUtilisateur) {
        this.statut = statut;
        this.sonUtilisateur = sonUtilisateur;
        this.sesMatieres = new ArrayList<Matiere>();
    }

    /**
     * Création d'un profil professeur
     * @param nom
     * @param prenom
     * @param adresseMail
     * @param motDePasse
     * @param dateDeNaissance
     * @param lienPhoto
     * @param statut
     * @return
     */
    public static Enseignant create(String nom, String prenom, String adresseMail, String motDePasse, String dateDeNaissance, String lienPhoto, String statut)
    {
        Utilisateur user =  Utilisateur.create(nom, prenom, adresseMail,motDePasse, dateDeNaissance, lienPhoto);
        Utilisateur.droitEnseignant(user.id); // Affecter le droit enseignant automatiquement
        Enseignant enseignant = new Enseignant(statut, user);

        enseignant.save();
        return enseignant;
    }

    /**
     * Mise à jour d'un profil enseignant
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
    public static Enseignant update(int id, String nom,String prenom,String adresseMail,String motDePasse,String dateDeNaissance,String lienPhoto, String statut) {
        Enseignant enseignant = find.where().eq("id", id).findUnique();

        if((statut != null) && (statut != ""))
        {
            enseignant.statut = statut;
        }

        Utilisateur.updateUtilisateur(enseignant.sonUtilisateur.id, nom, prenom, adresseMail, motDePasse, dateDeNaissance, lienPhoto);

        enseignant.update();

        return enseignant;

    }

    /**
     * Suppression du profil enseignant
     * @param id
     */
    public static void delete(long id)
    {
        // On retire son id des cours
        List<Cours> lesCoursAretirer = Cours.find.where().eq("son_enseignant_id",id).findList();

        if(lesCoursAretirer != null){
            for(Cours c : lesCoursAretirer){
                c.sonEnseignant = null;

                c.update();
            }
        }


        // Suppression dans la jointure enseignant_matiere
        Enseignant enseignant = find.where().eq("id", id).findUnique();

        Iterator<Matiere> itr = enseignant.sesMatieres.iterator();
        while(itr.hasNext()) {
            Matiere mat = itr.next();

                itr.remove();
        }

        enseignant.update();


        enseignant.delete();

        // suppression dans la table modules
        Utilisateur user =  enseignant.sonUtilisateur;

        if(user.sesModules.size()>1)
        {
            user.sesModules.remove(Module.findByLibelle("ENSEIGNANTS"));
            user.sesModules.remove(Module.findByLibelle("ADMINISTRATEURS"));
        }else{
            if(user.sesModules.size() == 1)
            {
                user.sesModules.remove(Module.findByLibelle("ENSEIGNANTS"));
            }
        }

        user.update();

        // Suppression dans la table utilisateur
        user.delete();
    }

    public static boolean utilisateurEnseignant(Utilisateur user)
    {
        return (Enseignant.find.where().eq("sonUtilisateur", user).findUnique()) != null ? true : false;
    }

    public static Enseignant findById (long id)
    {
        return find.ref(id);
    }

    public static Enseignant findByUser (long id){
        return find.where().eq("son_utilisateur_id", id).findUnique();
    }

    /**
     * Récupérer toute la liste des enseignants
     * Trié par ordre croissant suivant le nom
     * @return les enseignants
     */
    public static List<Enseignant> findAll() {
        List<Enseignant> lesEnseignants = find.all();

        // Trie des utilisateurs par ordre croissant par rapport à leur nom de famille
        Collections.sort(lesEnseignants, new Comparator<Enseignant>() {
            @Override
            public int compare(Enseignant tc1, Enseignant tc2) {
                return tc1.sonUtilisateur.nom.compareTo(tc2.sonUtilisateur.nom);
            }
        });

        return lesEnseignants;
    }

    @Override
    public String toString() {
        return "Enseignant{" +
                "id=" + id +
                ", statut='" + statut + '\'' +
                ", sonUtilisateur=" + sonUtilisateur +
                ", sesMatieres=" + sesMatieres +
                '}';
    }
}
