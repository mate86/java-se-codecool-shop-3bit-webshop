package com.codecool.shop.dao;

import com.codecool.shop.model.Order;
import java.sql.Date;

public interface OrderDao {

    void add(String name, String email, String phonenumber, String billingaddress, String shippingaddress, String description, Date date, int paymentMethod, int status);
    Order find(int id);
    void remove(int id);
}
