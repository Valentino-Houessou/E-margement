package models;

import javax.persistence.*;
import java.sql.Timestamp;
import com.avaje.ebean.*;

@Entity
public class Periode extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String libelle;
    public Timestamp dateDeDeut;
    public Timestamp dateFin;
}
