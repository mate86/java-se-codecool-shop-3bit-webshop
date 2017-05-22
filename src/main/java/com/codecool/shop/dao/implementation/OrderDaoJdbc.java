package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class OrderDaoJdbc implements OrderDao {
    private static final Logger logger = LoggerFactory.getLogger(OrderDaoJdbc.class);

    private static OrderDaoJdbc instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private OrderDaoJdbc() {
    }

    public static OrderDaoJdbc getInstance() {
        if (instance == null) {
            instance = new OrderDaoJdbc();
        }
        return instance;
    }

    public void add(String name, String email, String phonenumber, String billingaddress, String shippingaddress,
                    String description, Date date, int paymentMethod, int status) {
        logger.debug("Entering add()");
        String query = "INSERT INTO orders (name, email, phonenumber, billingaddress, shippingaddress, description, date, paymentMethod, status, userid)" +
                       " VALUES ('"+name+"', '"+email+"', '"+phonenumber+"', '"+billingaddress+"', '"+shippingaddress+"', '"+description+"', '"+date+"', "+paymentMethod+", "+status+", 0);";
        System.out.println(query);
        executeQuery(query);
        logger.debug("Query executed");
        logger.debug("Leaving add()");
    }

    public Order find(int id) {
        logger.debug("Entering find()");
        String query = "SELECT * FROM orders WHERE id = "+id+";";
        logger.debug("Leaving find()");
        return getDataFromDB(query);
    }

    public void remove(int id) {
        logger.debug("Entering remove()");
        String query = "DELETE FROM orders WHERE id = "+id+";";
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

    public Order getDataFromDB(String query) {
        logger.debug("Entering getDataFromDB()");

        try {
            Connection connection = DatabaseConnection.getConnection();
            logger.debug("Database connection is OK!");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                logger.debug("Leaving getDataFromDB()");
                return new Order(
                    resultSet.getInt("id"),
                    0,
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("phoneNumber"),
                    resultSet.getString("billingAddress"),
                    resultSet.getString("shippingAddress"),
                    resultSet.getString("description"),
                    resultSet.getInt("paymentMethod"),
                    resultSet.getDate("date"),
                    resultSet.getInt("status")
                );
            }

        } catch (SQLException e) {
            logger.error("Error! Getting data from database failed!", e);
        }
        logger.debug("Leaving getDataFromDB()");
        return new Order();
    }
}
