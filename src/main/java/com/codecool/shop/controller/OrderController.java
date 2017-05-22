package com.codecool.shop.controller;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.OrderDaoJdbc;
import com.codecool.shop.model.Cart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by flowerpower on 2017. 05. 04..
 */
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    public static ModelAndView checkout(Request req, Response res) {
        logger.debug("Entering checkout(req={}, res={})", req, res);
        Map params = new HashMap<>();
        Cart cart = new Cart();
        cart.initFromSession(req);
        logger.debug("Cart initialized from session");

        params.put("cart", cart);
        logger.debug("Params length: {}", params.size());
        logger.debug("Leaving checkout()");
        return new ModelAndView(params, "order/checkout");
    }

    public static ModelAndView payment(Request req, Response res) {
        logger.debug("Entering payment(req={}, res={})", req, res);

        //Instantiate needed variables
        Map params = new HashMap<>();
        Cart cart = new Cart();
        cart.initFromSession(req);
        logger.debug("Cart initialized from session");

        //Set changings on order and save
        logger.debug("Setting order's user");
        cart.order.setByUser(
                req.queryParams("name"),
                req.queryParams("email"),
                req.queryParams("phoneNumber"),
                req.queryParams("billingAddress"),
                req.queryParams("shippingAddress"),
                req.queryParams("description")
        );
        cart.order.setStatus(1);
        logger.debug("Setting order's status");
        cart.saveToSession(req);
        logger.debug("Cart saved to session");

        params.put("cart", cart);
        logger.debug("Params length: {}", params.size());
        logger.debug("Leaving payment()");
        return new ModelAndView(params, "order/payment");
    }

    public static ModelAndView confirmation(Request req, Response res) {
        logger.debug("Entering confirmation(req={}, res={})", req, res);

        //Instantiate needed variables
        Map params = new HashMap<>();
        Cart cart = new Cart();
        cart.initFromSession(req);
        logger.debug("Cart initialized from session");

        //Set changings on order and save
        cart.order.setPaymentMethod(Integer.parseInt(req.queryParams("payment")));
        logger.debug("Setting order's payment method OK");
        cart.order.setStatus(2);
        logger.debug("Setting order's status OK");
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
        logger.debug("Params length: {}", params.size());
        logger.debug("Leaving confirmation()");
        return new ModelAndView(params, "order/confirmation");
    }
}
