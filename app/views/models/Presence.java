package views.models;

import javax.persistence.*;
import com.avaje.ebean.*;

@Entity
public class Presence extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public boolean emergement;
    public String motif;
    public String justificatif;
    @ManyToOne(cascade=CascadeType.PERSIST)
    public Cours sonCours;
    @ManyToOne(cascade=CascadeType.PERSIST)
    public Etudiant sonEtudiant;

}
