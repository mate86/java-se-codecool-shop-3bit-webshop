package com.codecool.shop.model;

/**
 * Created by flowerpower on 2017. 05. 02..
 */
public class LineItem {
    public Product product;
    public int quantity;
    public Float price;

    public LineItem(Product product, int quantity, Float price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public float getYield() {
        return Math.round((price * quantity) * 100) / 100;
    }

    public float getVatPrice() {
        return Math.round((price * quantity) * 127) / 100;
    }

    public String toString() {
        return product.id + "," +
               quantity + "," +
               price;
    }
}
