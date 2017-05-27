package com.codecool.shop.controller;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.OrderDaoJdbc;
import com.codecool.shop.model.Cart;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by flowerpower on 2017. 05. 04..
 */

/**
 * This class handles the order: checkout, selecting payment method and confirm order.
 */

public class OrderController {

    /**
     * Loading cart content from database.
     */
    public static ModelAndView checkout(Request req, Response res) {
        Map params = new HashMap<>();
        Cart cart = new Cart();
        cart.initFromSession(req);
        params.put("cart", cart);

        return new ModelAndView(params, "order/checkout");
    }

    /**
     * Setting user's details.
     */
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
        cart.order.setStatus(1);
        cart.saveToSession(req);

        params.put("cart", cart);

        return new ModelAndView(params, "order/payment");
    }

    /**
     * Selecting payment method, confirm order.
     */
    public static ModelAndView confirmation(Request req, Response res) {

        //Instantiate needed variables
        Map params = new HashMap<>();
        Cart cart = new Cart();
        cart.initFromSession(req);

        //Set changings on order and save
        cart.order.setPaymentMethod(Integer.parseInt(req.queryParams("payment")));
        cart.order.setStatus(2);
        cart.dropSession(req.session());
        params.put("cart", cart);

        OrderDaoJdbc.getInstance().add(
                cart.order.getName(),
                cart.order.getEmail(),
                cart.order.getPhoneNumber(),
                cart.order.getBillingAddress(),
                cart.order.getShippingAddress(),
                cart.order.getDescription(),
                cart.order.getDate(),
                cart.order.getPaymentMethod().getId(),
                cart.order.getStatus()
        );
        return new ModelAndView(params, "order/confirmation");
    }
}
