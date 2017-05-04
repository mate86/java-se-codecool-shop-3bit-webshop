package com.codecool.shop.interfaces;

import spark.Request;

public interface Sessionable {
    void saveToSession(Request request);
    void initFromSession(Request request);
}