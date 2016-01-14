package models;
//TODO

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import com.avaje.ebean.*;
import com.sun.org.apache.xpath.internal.operations.Mod;

@Entity
public class Utilisateur extends Model  {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public long id;
    public String nom;
    public String prenom;
    public String adresseMail;
    public String motDePasse;
    @Column(columnDefinition = "datetime")
    public Timestamp dateDeNaissance;
    public String lienPhoto;

    @ManyToMany(cascade=CascadeType.PERSIST)
    public List<Module> sesModules;

    public Utilisateur(String nom, String prenom, String adresseMail, String motDePasse,
                       Timestamp dateDeNaissance, String lienPhoto) {
        this.nom = nom;
        this.prenom = prenom;
        this.adresseMail = adresseMail;
        this.motDePasse = motDePasse;
        this.dateDeNaissance = dateDeNaissance;
        this.lienPhoto = lienPhoto;
        this.sesModules = new ArrayList<Module>();
    }

    //Use to create an user
    public static Utilisateur create(String nom, String prenom, String adresseMail, String motDePasse,
                                     String dateDeNaissance, String lienPhoto){
        Timestamp ddn = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date ddnd = formatter.parse(dateDeNaissance);
            ddn = new Timestamp(ddnd.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Utilisateur user = new Utilisateur(nom, prenom, adresseMail,
                                        motDePasse, ddn, lienPhoto);
        user.save();
        return user;
    }

    public static Utilisateur updateUtilisateur(long id, String nom, String prenom, String adresseMail, String motDePasse,
                                     String dateDeNaissance, String lienPhoto, List<Module> sesModules){
        Utilisateur user = find.ref(id);
        if (nom != null)
            user.nom = nom;
        if (prenom != null)
            user.prenom = prenom;
        if (adresseMail != null)
            user.adresseMail = adresseMail;
        if (motDePasse != null)
            user.motDePasse = motDePasse;
        if (dateDeNaissance != null) {
            Timestamp ddn = null;
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date ddnd = formatter.parse(dateDeNaissance);
                ddn = new Timestamp(ddnd.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            user.dateDeNaissance = ddn;
        }
        if (lienPhoto != null)
            user.lienPhoto = lienPhoto;
        if (sesModules != null)
            user.sesModules = sesModules;

        user.update();

        return user;
    }

    public static void deleteUtilisateur(long id){
        Utilisateur user = find.ref(id);
        if (Etudiant.utilisateurEtudiant(user) ||
                Enseignant.utilisateurEnseignant(user) ||
                Admin.utilisateurAdmin(user))
            user.delete();
    }

    public void droitEnseignant (){
        this.sesModules.add(Module.findByLibelle("ENSEIGNANTS"));
        this.update();
    }

    public void droitEtudiant (){
        this.sesModules.add(Module.findByLibelle("ETUDIANTS"));
        this.update();
    }

    public void droitAdmin (){
        this.sesModules.add(Module.findByLibelle("ADMINISTRATEURS"));
        this.update();
    }


    //Finder for retrieve data in database
    public static Finder<Long,Utilisateur> find = new Finder<Long,Utilisateur>(Utilisateur.class);

    public static List<Utilisateur> findAll(){
        return Utilisateur.find.all();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilisateur)) return false;

        Utilisateur that = (Utilisateur) o;

        if (id != that.id) return false;
        if (!nom.equals(that.nom)) return false;
        if (!prenom.equals(that.prenom)) return false;
        if (!adresseMail.equals(that.adresseMail)) return false;
        if (!motDePasse.equals(that.motDePasse)) return false;
        return !(dateDeNaissance != null ? !dateDeNaissance.equals(that.dateDeNaissance) : that.dateDeNaissance != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + nom.hashCode();
        result = 31 * result + prenom.hashCode();
        result = 31 * result + adresseMail.hashCode();
        result = 31 * result + motDePasse.hashCode();
        result = 31 * result + (dateDeNaissance != null ? dateDeNaissance.hashCode() : 0);
        return result;
    }
}
