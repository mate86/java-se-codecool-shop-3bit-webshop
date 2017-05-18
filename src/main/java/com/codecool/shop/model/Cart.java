package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.Date;

import com.codecool.shop.dao.implementation.ProductDaoJdbc;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.interfaces.*;
import spark.Request;
import spark.Session;

/**
 * Created by flowerpower on 2017. 05. 02..
 */
public class Cart implements Sessionable {

    public Order order;
    public ArrayList<LineItem> lineItem;

    public Cart() {
        this.order = new Order(0, "");
        this.lineItem = new ArrayList<>();
    }

    public void addToCart(Product product, int quantity) {
        if (isProductInCart(product)) {
            lineItem.get(productIndexInCart(product)).quantity += quantity;
        } else {
            lineItem.add(new LineItem(product, quantity, product.getDefaultPrice()));
        }
    }

    public void modifyItem(Product product, int quantity) {
        if (isProductInCart(product)) {
            lineItem.get(productIndexInCart(product)).quantity = quantity;
        }
    }

    public void removeItem(Product product) {
        if (isProductInCart(product)) {
            lineItem.remove(productIndexInCart(product));
        }
    }

    public void dropSession(Session session) {
        session.removeAttribute("cart");
    }

    public LineItem getProduct(int index) {
        return lineItem.get(index);
    }

    public ArrayList<LineItem> getProducts() {
        return lineItem;
    }

    public Integer getCartSize() {
        int cartsize = 0;
        for (int item = 0; item < lineItem.size(); item++) {
            cartsize += lineItem.get(item).quantity;
        }
        return cartsize;
    }

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
            order = request.session().attribute("order");

        }
    }

    public void initLineItemsFromSession(Request request) {
        if (request.session().attribute("lineitem") != "" && request.session().attribute("lineitem") != null) {
            String[] products = request.session().attribute("lineitem").toString().split(";");

            for (int i = 0; i < products.length; i++) {
                String[] tmpProduct = products[i].split(",");

                lineItem.add(new LineItem(
                        ProductDaoJdbc.getInstance().find(Integer.parseInt(tmpProduct[0])),
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
            request.session().attribute("lineitem", sessionValue.substring(0, sessionValue.length() - 1));
        } else {
            request.session().attribute("lineitem", sessionValue);
        }
    }

    public boolean isProductInCart(Product product) {
        for (int i = 0; i < lineItem.size(); i++) {
            if (lineItem.get(i).product == product) {
                return true;
            }
        }
        return false;
    }

    public int productIndexInCart(Product product) {
        int index = 0;

        for (int i = 0; i < lineItem.size(); i++) {
            if (lineItem.get(i).product == product) {
                index = i;
            }
        }

        return index;
    }

    public float countTotal() {
        float total = 0;
        for (int i = 0; i < lineItem.size(); i++) {
            total += lineItem.get(i).getVatPrice();
        }
        return total;
    }

    public boolean isEmpty() {
        if (lineItem.size() == 0) {
            return true;
        }
        return false;
    }
}
