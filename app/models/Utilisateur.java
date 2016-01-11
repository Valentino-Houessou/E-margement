package models;

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

    public void update(String nom, String prenom, String adresse_mail, String mot_de_passe, String date_de_naissance, String lien_photo) {
            this.nom = nom;
            this.prenom = prenom;
            this.adresseMail = adresse_mail;
            this.motDePasse = mot_de_passe;
            this.dateDeNaissance = Timestamp.valueOf(date_de_naissance);
            this.lienPhoto = lien_photo;
            Ebean.save(this);
    }
}
