package models;

//TODO
import javax.persistence.*;
import com.avaje.ebean.*;

public class Admin  extends Model {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int id;
    public String statut;
    @OneToOne
    public int utilisateur_id;

}

