package models;
//TODO

import java.sql.Timestamp;
import java.util.Collection;
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
    public Timestamp dateDeNaissance;
    public String lienPhoto;

    @ManyToMany(cascade=CascadeType.ALL)
    public Collection<Module> sesModules;

}
