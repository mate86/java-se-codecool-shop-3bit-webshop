package com.codecool.shop.controller;

import com.codecool.shop.model.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by flowerpower on 2017. 05. 07..
 */
public class UserController {

    public enum pageName {
        LOGIN, REGISTRATION
    }

    public static ModelAndView loginGet(Request req, Response res) {
        Map params = new HashMap<>();
        params.put("page", pageName.LOGIN);

        return new ModelAndView(params, "user/login");
    }

    public static ModelAndView loginPost(Request req, Response res) {
        Map params = new HashMap<>();

        User user = User.auth(req.queryParams("user"), req.queryParams("password"));
        if (!user.getUsername().equals("notfound")) {
            user.saveToSession(req.session());
        }
        System.out.println(user.getUsername());

        params.put("page", pageName.LOGIN);
        params.put("user", user);
        return new ModelAndView(params, "user/login");
    }

    public static ModelAndView registrationGet(Request req, Response res) {
        Map params = new HashMap<>();
        params.put("page", pageName.REGISTRATION);

        return new ModelAndView(params, "user/registration");
    }

    public static ModelAndView registrationPost(Request req, Response res) {
        Map params = new HashMap<>();
        params.put("page", pageName.REGISTRATION);

        return new ModelAndView(params, "user/registration");
    }
}
