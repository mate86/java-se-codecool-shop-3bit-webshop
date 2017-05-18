package com.codecool.shop.model;

/**
 * Created by flowerpower on 2017. 05. 02..
 */
public class Status extends BaseModel {

    public Status() {
        super("");
        setId(0);
    }

    public Status(int id, String name) {
        super(name);
        setId(id);
    }

}
