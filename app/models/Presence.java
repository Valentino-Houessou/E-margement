package models;

import javax.persistence.*;
import com.avaje.ebean.*;

/**
 * Created by Bigval-Mac on 08/01/16.
 */

@Entity
public class Presence extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public boolean emergement;
    public String motif;
    public String justificatif;
}
