package models;
//TODO

import java.sql.Timestamp;
import java.util.List;
import javax.persistence.*;
import com.avaje.ebean.*;

@Entity
public class Utilisateur extends Model  {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int id;
    public String nom;
    public String prenom;
    public String adresseMail;
    public String motDePasse;
    @Column(columnDefinition = "datetime")
    public Timestamp dateDeNaissance;
    public String lienPhoto;

    @ManyToMany(cascade=CascadeType.PERSIST)
    public List<Module> sesModules;

    public Utilisateur(String nom, String prenom, String adresseMail, String motDePasse, String dateDeNaissance, String lienPhoto) {
        this.nom = nom;
        this.prenom = prenom;
        this.adresseMail = adresseMail;
        this.motDePasse = motDePasse;
        this.dateDeNaissance = Timestamp.valueOf(dateDeNaissance);
        this.lienPhoto = lienPhoto;
    }
}
