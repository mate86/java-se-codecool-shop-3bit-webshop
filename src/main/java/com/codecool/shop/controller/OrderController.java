package com.codecool.shop.controller;

import com.codecool.shop.model.Cart;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by flowerpower on 2017. 05. 04..
 */
public class OrderController {
    public static ModelAndView checkout(Request req, Response res) {
        Map params = new HashMap<>();
        Cart cart = new Cart();
        cart.initFromSession(req);
        params.put("cart", cart);

        return new ModelAndView(params, "order/checkout");
    }

    public static ModelAndView payment(Request req, Response res) {

        //Instantiate needed variables
        Map params = new HashMap<>();
        Cart cart = new Cart();
        cart.initFromSession(req);

        //Set changings on order and save
        cart.order.setByUser(
                req.queryParams("name"),
                req.queryParams("email"),
                req.queryParams("phoneNumber"),
                req.queryParams("billingAddress"),
                req.queryParams("shippingAddress"),
                req.queryParams("description")
        );
        cart.saveToSession(req);

        params.put("cart", cart);

        return new ModelAndView(params, "order/payment");
    }

    public static ModelAndView confirmation(Request req, Response res) {

        //Instantiate needed variables
        Map params = new HashMap<>();
        Cart cart = new Cart();
        cart.initFromSession(req);

        //Set changings on order and save
        cart.order.setByUser(
                req.queryParams("name"),
                req.queryParams("email"),
                req.queryParams("phoneNumber"),
                req.queryParams("billingAddress"),
                req.queryParams("shippingAddress"),
                req.queryParams("description")
        );
        cart.saveToSession(req);


        params.put("cart", cart);

        return new ModelAndView(params, "order/confirmation");
    }

}
