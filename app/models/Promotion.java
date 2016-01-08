package models;

import javax.persistence.*;
import com.avaje.ebean.*;
import java.util.Collection;

<<<<<<< HEAD
=======
import java.util.Collection;

/**
 * Created by Bigval-Mac on 08/01/16.
 */
>>>>>>> 071c8d0c8d795db0286870e3531f7cd8cfeee99a
@Entity
public class Promotion extends Model{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String anneeScolaire;
    public String groupe;
    public String type;
<<<<<<< HEAD
    @ManyToMany(cascade=CascadeType.ALL)
    public Collection<Etudiant> sesEtudiants;
=======
    public String filiere;
    @ManyToMany
    public Collection<Matiere> sesMatieres;

>>>>>>> 071c8d0c8d795db0286870e3531f7cd8cfeee99a
}
