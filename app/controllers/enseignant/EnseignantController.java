package controllers.enseignant;

import models.Enseignant;
import play.*;
import play.mvc.*;

import views.html.enseignant.indexEnseignant;

public class EnseignantController extends Controller{

    private Enseignant user;

    public  Result getListEnseignant() {
        return Results.TODO;
    }

    public  Result ajoutProf() {
        return Results.TODO;
    }

    public  Result modifProf() {
        return Results.TODO;
    }

    public  Result supProf() {
        return Results.TODO;
    }

    public  Result listPresent() {
        return Results.TODO;
    }

    public Result index() {
        return ok(indexEnseignant.render("Your new application is ready.",user));

    }



}
