package models;

//TODO
import javax.persistence.*;

public class Admin {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int id;
    public String statut;

}

