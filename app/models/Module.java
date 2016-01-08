package models;


import javax.persistence.*;
/*
import play.db.ebean.*;
import com.avaje.ebean.*;
*/

/**
 * Created by Bigval-Mac on 05/01/16.
 */
public class Module{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public long id;
    public String libelle;
}
