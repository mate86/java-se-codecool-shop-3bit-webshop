package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.ProductDaoJdbc;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.model.Cart;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class CartController {

    /**
     * This class handles the cart's content: adds products to cart from database, removes, and modifies quantity.
     */

    public static ModelAndView renderProducts(Request req, Response res) {
        Map params = new HashMap<>();
        Cart cart = new Cart();
        cart.initFromSession(req);

        params.put("cart", cart);
        return new ModelAndView(params, "cart/list");
    }

    public static String addProduct(Request req, Response res) {
        ProductDao productDataStore = ProductDaoJdbc.getInstance();
        Cart cart = new Cart();
        cart.initFromSession(req);

        int id = Integer.parseInt(req.params("id"));
        cart.addToCart(productDataStore.find(id), 1);
        cart.saveToSession(req);

        Map params = new HashMap<>();
        params.put("cart", cart);
        return cart.getCartSize().toString();
    }

    public static ModelAndView removeProduct(Request req, Response res) {
        ProductDao productDataStore = ProductDaoJdbc.getInstance();
        Cart cart = new Cart();
        cart.initFromSession(req);

        int id = Integer.parseInt(req.params("id"));
        cart.removeItem(productDataStore.find(id));
        cart.saveToSession(req);

        Map params = new HashMap<>();
        params.put("cart", cart);
        return new ModelAndView(params, "cart/list");
    }

    public static ModelAndView modifyProduct(Request req, Response res) {
        ProductDao productDataStore = ProductDaoJdbc.getInstance();
        Cart cart = new Cart();
        cart.initFromSession(req);

        int id = Integer.parseInt(req.params("id"));
        int quantity = Integer.parseInt(req.queryParams("quantity"));
        cart.modifyItem(productDataStore.find(id), quantity);
        cart.saveToSession(req);

        Map params = new HashMap<>();
        params.put("cart", cart);
        return new ModelAndView(params, "cart/list");
    }
}
