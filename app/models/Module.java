package models;

import javax.persistence.*;

/*
import play.db.ebean.*;
import com.avaje.ebean.*;
*/

@Entity
public class Module{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public long id;
    public String libelle;
}
