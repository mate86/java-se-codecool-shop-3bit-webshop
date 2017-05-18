package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Order;
import java.sql.*;

public class OrderDaoJdbc implements OrderDao {

    private static OrderDaoJdbc instance = null;

    private static final String DATABASE = "jdbc:postgresql://localhost:5432/codecoolshop";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

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

    public void add(String name, String email, String phonenumber, String billingaddress, String shippingaddress, String description, Date date, int paymentMethod, int status) {
        String query = "INSERT INTO orders (name, email, phonenumber, billingaddress, shippingaddress, description, date, paymentMethod, status, userid)" +
                       " VALUES ('"+name+"', '"+email+"', '"+phonenumber+"', '"+billingaddress+"', '"+shippingaddress+"', '"+description+"', '"+date+"', "+paymentMethod+", "+status+", 0);";
        System.out.println(query);
        executeQuery(query);
    }

    public Order find(int id) {
        String query = "SELECT * FROM orders WHERE id = "+id+";";
        return getDataFromDB(query);
    }

    public void remove(int id) {
        String query = "DELETE FROM orders WHERE id = "+id+";";
        executeQuery(query);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DATABASE,
                DB_USER,
                DB_PASSWORD);
    }

    private void executeQuery(String query) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
        ) {
            statement.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Order getDataFromDB(String query) {

        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
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
            e.printStackTrace();
        }
        return new Order();
    }
}
