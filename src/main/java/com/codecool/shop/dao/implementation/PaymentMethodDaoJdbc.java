package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.PaymentMethodDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.PaymentMethod;
import com.codecool.shop.model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentMethodDaoJdbc implements PaymentMethodDao {

    private static PaymentMethodDaoJdbc instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private PaymentMethodDaoJdbc() {
    }

    public static PaymentMethodDaoJdbc getInstance() {
        if (instance == null) {
            instance = new PaymentMethodDaoJdbc();
        }
        return instance;
    }

    public void add(String name) {
        String query = "INSERT INTO paymentmethods (name) VALUES ('"+name+"');";
        executeQuery(query);
    }

    public PaymentMethod find(int id) {
        String query = "SELECT * FROM paymentmethods WHERE id = "+id+";";
        return getDataFromDB(query);
    }

    public void remove(int id) {
        String query = "DELETE FROM paymentmethods WHERE id = "+id+";";
        executeQuery(query);
    }

    private void executeQuery(String query) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
        ) {
            statement.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PaymentMethod getDataFromDB(String query) {

        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                return new PaymentMethod(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PaymentMethod();
    }
}
