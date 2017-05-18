package com.codecool.shop.dao;

import com.codecool.shop.model.Status;

public interface StatusDao {

    void add(String name);
    Status find(int id);
    void remove(int id);
}
