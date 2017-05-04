package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import spark.Request;
import spark.Response;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public class ProductController {

    public static ModelAndView renderProducts(Request req, Response res) {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();

        Map params = new HashMap<>();
        params.put("categories", productCategoryDataStore.getAll());
        params.put("suppliers", supplierDataStore.getAll());
        if(req.queryParams("category")!=null) {
            params.put("filter", productCategoryDataStore.find(Integer.parseInt(req.queryParams("category"))));
            params.put("products", productDataStore.getBy(productCategoryDataStore.find(Integer.parseInt(req.queryParams("category")))));
        }
        else if(req.queryParams("supplier")!=null) {
            params.put("filter", supplierDataStore.find(Integer.parseInt(req.queryParams("supplier"))));
            params.put("products", productDataStore.getBy(supplierDataStore.find(Integer.parseInt(req.queryParams("supplier")))));
        }
        else{
            params.put("filter", productCategoryDataStore.find(1));
            params.put("products", productDataStore.getAll());
        }
        return new ModelAndView(params, "product/index");
    }

}
