package com.codecool.shop.model;

import java.util.Date;

/**
 * Created by flowerpower on 2017. 05. 02..
 */
public class Order {
    public int id;
    public int userId;
    public String description;
    public Date date;
    public String status;

    public Order(int userId, String description) {
        this.id = 1;
        this.userId = userId;
        this.description = description;
        this.date = new Date();
        this.status = "IN PROGRESS";
    }

    public Order(int id, int userId, String description, Date date, String status) {
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    public String toString() {
        return id + ";"+
               userId + ";" +
               description + ";" +
               date.getTime() + ";" +
               status;
    }
}
