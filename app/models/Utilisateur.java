package models;
//TODO

import java.sql.Timestamp;
import javax.persistence.*;

@Entity
public class Utilisateur  {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int idUtilisateur;
    public String nom;
    public String prenom;
    public String adresseMail;
    public String motDePasse;
    public Timestamp dateDeNaissance;
    public String lienPhoto;

}
