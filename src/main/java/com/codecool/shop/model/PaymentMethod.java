package com.codecool.shop.model;

/**
 * Created by flowerpower on 2017. 05. 02..
 */
public class PaymentMethod extends BaseModel {

    public PaymentMethod() {
        super("");
        setId(0);
    }

    public PaymentMethod(int id, String name) {
        super(name);
        setId(id);
    }

}
