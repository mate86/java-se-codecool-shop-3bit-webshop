package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.StatusDao;
import com.codecool.shop.model.Status;

import java.sql.*;

public class StatusDaoJdbc implements StatusDao {

    private static StatusDaoJdbc instance = null;

    private static final String DATABASE = "jdbc:postgresql://localhost:5432/codecoolshop";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    /* A private Constructor prevents any other class from instantiating.
     */
    private StatusDaoJdbc() {
    }

    public static StatusDaoJdbc getInstance() {
        if (instance == null) {
            instance = new StatusDaoJdbc();
        }
        return instance;
    }

    public void add(String name) {
        String query = "INSERT INTO statuses (name) VALUES ('"+name+"');";
        executeQuery(query);
    }

    public Status find(int id) {
        String query = "SELECT * FROM statuses WHERE id = "+id+";";
        return getDataFromDB(query);
    }

    public void remove(int id) {
        String query = "DELETE FROM status WHERE id = "+id+";";
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

    public Status getDataFromDB(String query) {

        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                return new Status(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Status();
    }
}
