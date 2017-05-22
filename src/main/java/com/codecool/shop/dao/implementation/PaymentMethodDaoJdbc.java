package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.PaymentMethodDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.PaymentMethod;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentMethodDaoJdbc implements PaymentMethodDao {
    private static final Logger logger = LoggerFactory.getLogger(PaymentMethodDaoJdbc.class);

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
        logger.debug("Entering add()");
        String query = "INSERT INTO paymentmethods (name) VALUES ('"+name+"');";
        executeQuery(query);
        logger.debug("Query executed");
        logger.debug("Leaving add()");
    }

    public PaymentMethod find(int id) {
        logger.debug("Entering find()");
        String query = "SELECT * FROM paymentmethods WHERE id = "+id+";";
        logger.debug("Leaving find()");
        return getDataFromDB(query);
    }

    public void remove(int id) {
        logger.debug("Entering remove()");
        String query = "DELETE FROM paymentmethods WHERE id = "+id+";";
        executeQuery(query);
        logger.debug("Query executed");
        logger.debug("Leaving remove()");
    }

    private void executeQuery(String query) {
        logger.debug("Entering executeQuery()");
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
        ) {
            statement.execute(query);
            logger.debug("Query executed");

        } catch (SQLException e) {
            logger.error("Error! Query execution failed!", e);
        }
        logger.debug("Leaving executeQuery()");
    }

    public PaymentMethod getDataFromDB(String query) {
        logger.debug("Entering getDataFromDB()");

        try {
            Connection connection = DatabaseConnection.getConnection();
            logger.debug("Database connection is OK!");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                logger.debug("Leaving getDataFromDB()");
                return new PaymentMethod(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );
            }

        } catch (SQLException e) {
            logger.error("Error! Getting data from database failed!", e);
        }
        logger.debug("Leaving getDataFromDB()");
        return new PaymentMethod();
    }
}
