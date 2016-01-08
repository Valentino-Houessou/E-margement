package models;
//TODO

import javax.persistence.*;
import com.avaje.ebean.*;

import java.util.Collection;

/**
 * Created by Bigval-Mac on 08/01/16.
 */
@Entity
public class Enseignant extends Model{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int id;
    public String statut;
<<<<<<< HEAD
    @OneToOne
    public int utilisateur_id;
=======
    @ManyToMany
    public Collection<Matiere> sesMatieres;
>>>>>>> 071c8d0c8d795db0286870e3531f7cd8cfeee99a

}
