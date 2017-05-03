package com.codecool.shop.model;

import com.codecool.shop.dao.implementation.ProductDaoMem;
import spark.Request;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by flowerpower on 2017. 05. 02..
 */
public abstract class AbstractCart {
    protected Order order;
    protected ArrayList<LineItem> lineItem;

    public abstract void addToCart(Product product, int quantity);
    public abstract void modifyItem(Product product, int quantity);
    public abstract void removeItem(Product product);
    public abstract ArrayList<LineItem> getProducts();
    public abstract LineItem getProduct(int index);

    public void initFromSession(Request request) {
        initOrderFromSession(request);
        initLineItemsFromSession(request);

    }

    public void saveToSession(Request request) {
        saveOrderToSession(request);
        saveProductsToSession(request);

    }

    // *** AID METHODS ***
    public void initOrderFromSession(Request request) {
        if (request.session().attribute("order") != null) {
            String[] sessionValues = request.session().attribute("order").toString().split(";");

            Date orderDate = new Date();
            orderDate.setTime(Long.parseLong(sessionValues[3]));

            order = new Order(
                    Integer.parseInt(sessionValues[0]),
                    Integer.parseInt(sessionValues[1]),
                    sessionValues[2],
                    orderDate,
                    sessionValues[4]
            );

        }
    }

    public void initLineItemsFromSession(Request request) {
        if (request.session().attribute("lineitem") != null) {
            String[] products = request.session().attribute("lineitem").toString().split(";");

            for (int i = 0; i < products.length; i++) {
                String[] tmpProduct = products[i].split(",");

                lineItem.add(new LineItem(
                        ProductDaoMem.getInstance().find(Integer.parseInt(tmpProduct[0])),
                        Integer.parseInt(tmpProduct[1]),
                        Float.parseFloat(tmpProduct[2])
                ));
            }

        }
    }

    public void saveOrderToSession(Request request) {
        request.session().attribute("order", order);
    }

    public void saveProductsToSession(Request request) {
        String sessionValue = "";

        for (int i = 0; i < lineItem.size(); i++) {
            sessionValue += lineItem.get(i) + ";";
        }

        if (sessionValue.trim() != "") {
            request.session().attribute("lineitem", sessionValue.substring(0, sessionValue.length()-1));
        }
    }
}
