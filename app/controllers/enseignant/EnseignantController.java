package controllers.enseignant;

import models.Enseignant;
import play.*;
import play.mvc.*;

import views.html.index;

public class EnseignantController extends Controller{

    private Enseignant user;

    public Result index() {
        return ok(index.render("Your new application is ready."));

    }

    public Result getListEnseignant(){
        System.out.println(Enseignant.showall());
        return ok();
    }

    public Result getEnseignant(){
        System.out.println(Enseignant.show(2));
        return ok();
    }

}
