import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

import com.codecool.shop.controller.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main {

    public static void main(String[] args) {

        // default server settings
        exception(Exception.class, (e, req, res) -> e.printStackTrace());
        staticFileLocation("/public");
        port(8888);

        get("/", IndexController::homePage, new ThymeleafTemplateEngine());
        get("/faq", IndexController::faq, new ThymeleafTemplateEngine());

        get("/login", UserController::loginGet, new ThymeleafTemplateEngine());
        post("/login", UserController::loginPost, new ThymeleafTemplateEngine());

        get("/registration", UserController::registrationGet, new ThymeleafTemplateEngine());
        post("/registration", UserController::registrationPost, new ThymeleafTemplateEngine());

        // Add this line to your project to enable the debug screen
        enableDebugScreen();
    }
}
