package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.model.Cart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    public static ModelAndView renderProducts(Request req, Response res) {
        logger.debug("Entering renderProducts(req={}, res={})", req, res);
        ProductDao productDataStore = ProductDaoJdbc.getInstance();
        SupplierDao supplierDataStore = SupplierDaoJdbc.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJdbc.getInstance();
        Cart cart = new Cart();
        cart.initFromSession(req);
        logger.debug("Cart initialized from session");

        Map params = new HashMap<>();
        params.put("cart", cart);
        params.put("categories", productCategoryDataStore.getAll());
        params.put("suppliers", supplierDataStore.getAll());
        if (req.queryParams("category") != null) {
            logger.debug("Filtering products by category");
            params.put("filter", productCategoryDataStore.find(Integer.parseInt(req.queryParams("category"))));
            params.put("products", productDataStore.getBy(productCategoryDataStore.find(Integer.parseInt(req.queryParams("category")))));
        } else if (req.queryParams("supplier") != null) {
            logger.debug("Filtering products by supplier");
            params.put("filter", supplierDataStore.find(Integer.parseInt(req.queryParams("supplier"))));
            params.put("products", productDataStore.getBy(supplierDataStore.find(Integer.parseInt(req.queryParams("supplier")))));
        } else {
            logger.debug("No filtering products");
            params.put("filter", productCategoryDataStore.find(1));
            params.put("products", productDataStore.getAll());
        }
        logger.debug("Params length: {}", params.size());
        logger.debug("Leaving renderProducts()");
        return new ModelAndView(params, "product/index");
    }

}
