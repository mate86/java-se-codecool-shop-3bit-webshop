package com.codecool.shop.model;

import java.util.ArrayList;

/**
 * Created by flowerpower on 2017. 05. 02..
 */
public class Cart extends AbstractCart {

    public Cart() {
        this.order = new Order(0, "");
        this.lineItem = new ArrayList<>();
    }

    public void addToCart(Product product, int quantity) {
        if (isProductInCart(product)) {
            System.out.println("bejött");
            modifyItem(product, quantity);
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

    public ArrayList<LineItem> getProducts() {
        return lineItem;
    }

    public LineItem getProduct(int index) {
       return lineItem.get(index);
    }

    // *** AID METHODS ***
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
}
