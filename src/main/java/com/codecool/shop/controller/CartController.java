package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.ProductDaoJdbc;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.model.Cart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by flowerpower on 2017. 05. 02..
 */
public class CartController {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    public static ModelAndView renderProducts(Request req, Response res) {
        logger.debug("Entering renderProducts(req={}, res={})", req, res);
        Map params = new HashMap<>();
        Cart cart = new Cart();
        cart.initFromSession(req);

        params.put("cart", cart);
        logger.debug("Params length: {}", params.size());
        logger.debug("Leaving renderProducts()");
        return new ModelAndView(params, "cart/list");
    }

    public static String addProduct(Request req, Response res) {
        logger.debug("Entering addProduct(req={}, res={})", req, res);
        ProductDao productDataStore = ProductDaoJdbc.getInstance();
        Cart cart = new Cart();
        cart.initFromSession(req);
        logger.debug("Cart initialized from session");

        int id = Integer.parseInt(req.params("id"));
        cart.addToCart(productDataStore.find(id), 1);
        logger.debug("Product added to cart");
        cart.saveToSession(req);
        logger.debug("Cart saved to session");

        Map params = new HashMap<>();
        params.put("cart", cart);
        logger.debug("Params length: {}", params.size());
        logger.debug("Leaving addProduct()");
        return cart.getCartSize().toString();
    }

    public static ModelAndView removeProduct(Request req, Response res) {
        logger.debug("Entering removeProduct(req={}, res={})", req, res);
        ProductDao productDataStore = ProductDaoJdbc.getInstance();
        Cart cart = new Cart();
        cart.initFromSession(req);
        logger.debug("Cart initialized from session");

        int id = Integer.parseInt(req.params("id"));
        cart.removeItem(productDataStore.find(id));
        logger.debug("Product removed from cart");
        cart.saveToSession(req);
        logger.debug("Cart saved to session");

        Map params = new HashMap<>();
        params.put("cart", cart);
        logger.debug("Params length: {}", params.size());
        logger.debug("Leaving removeProduct()");
        return new ModelAndView(params, "cart/list");
    }

    public static ModelAndView modifyProduct(Request req, Response res) {
        logger.debug("Entering modifyProduct(req={}, res={})", req, res);
        ProductDao productDataStore = ProductDaoJdbc.getInstance();
        Cart cart = new Cart();
        cart.initFromSession(req);
        logger.debug("Cart initialized from session");

        int id = Integer.parseInt(req.params("id"));
        int quantity = Integer.parseInt(req.queryParams("quantity"));
        cart.modifyItem(productDataStore.find(id), quantity);
        logger.debug("Product quantity modified in cart");
        cart.saveToSession(req);
        logger.debug("Cart saved to session");

        Map params = new HashMap<>();
        params.put("cart", cart);
        logger.debug("Params length: {}", params.size());
        logger.debug("Leaving modifyProduct()");
        return new ModelAndView(params, "cart/list");
    }
}
