package models;


import javax.persistence.*;
import com.avaje.ebean.*;

@Entity
public class Admin  extends Model {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int id;
    public String statut;
    @OneToOne
    public Utilisateur sonUtilisateur;

}

