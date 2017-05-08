package com.codecool.shop.controller;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by flowerpower on 2017. 05. 02..
 */
public class IndexController {

    public enum pageName {
        INDEX, FAQ
    }

    public static ModelAndView homePage(Request req, Response res) {
        Map params = new HashMap<>();
        params.put("page", pageName.INDEX);

        return new ModelAndView(params, "index");
    }

    public static ModelAndView faq(Request req, Response res) {
        Map params = new HashMap<>();
        params.put("page", pageName.FAQ);

        return new ModelAndView(params, "faq/index");
    }
}
