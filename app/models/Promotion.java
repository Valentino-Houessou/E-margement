package models;

import javax.persistence.*;
import com.avaje.ebean.*;

import java.util.Collection;

/**
 * Created by Bigval-Mac on 08/01/16.
 */
@Entity
public class Promotion extends Model{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String anneeScolaire;
    public String groupe;
    public String type;
    public String filiere;
    @ManyToMany
    public Collection<Matiere> sesMatieres;

}
