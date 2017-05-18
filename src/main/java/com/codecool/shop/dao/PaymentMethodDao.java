package com.codecool.shop.dao;

import com.codecool.shop.model.PaymentMethod;
import com.codecool.shop.model.Supplier;

import java.util.List;

public interface PaymentMethodDao {

    void add(String name);
    PaymentMethod find(int id);
    void remove(int id);
}
