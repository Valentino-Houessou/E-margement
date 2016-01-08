package models;
//TODO

import javax.persistence.*;
import com.avaje.ebean.*;


@Entity
public class Module extends Model{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public long id;
    public String libelle;
}
