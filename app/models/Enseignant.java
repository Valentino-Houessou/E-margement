package models;
//TODO

import javax.persistence.*;
import com.avaje.ebean.*;

/**
 * Created by Bigval-Mac on 08/01/16.
 */
@Entity
public class Enseignant extends Model{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int id;
    public String statut;
    @OneToOne
    public int utilisateur_id;

}
