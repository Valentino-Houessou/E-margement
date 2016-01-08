package models;

import javax.persistence.*;
import java.sql.Timestamp;
import com.avaje.ebean.*;

@Entity
public class Cours extends Model{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String type;
    public Timestamp heureDebut;
    public Timestamp heureFin;
}
