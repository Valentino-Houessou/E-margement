package models;
//TODO
import javax.persistence.*;
import com.avaje.ebean.*;

@Entity
public class Etudiant extends Model{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int id;
    public String numeroEtudiant;
    public String status;
}
