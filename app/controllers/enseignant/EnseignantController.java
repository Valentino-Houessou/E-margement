package controllers.enseignant;

import models.Enseignant;
import play.*;
import play.mvc.*;

import views.html.enseignant.indexEnseignant;

public class EnseignantController extends Controller{

    private Enseignant user;

    public Result index() {
        return ok(indexEnseignant.render("Your new application is ready.",user));

    }



}
