package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.*;
import play.mvc.*;

import views.html.*;

import java.text.ParseException;

public class Application extends Controller {

    public Result index() {
        return ok(index.render("Your new application is ready."));
    }
}
