package models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

import com.avaje.ebean.*;

@Entity
public class Cours extends Model{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String type;
    public Timestamp heureDebut;
    public Timestamp heureFin;
    @ManyToOne
    public Enseignant sonEnseignant;
    @ManyToOne
    public Matiere saMatiere;
    @ManyToOne
    public Salle saSalle;
    @ManyToOne
    public Periode saPeriode;
    @OneToMany(mappedBy = "sonCours")
    public Collection<Presence> sesPresences;
}
