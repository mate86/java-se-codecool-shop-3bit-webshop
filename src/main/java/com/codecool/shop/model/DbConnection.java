package com.codecool.shop.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by flowerpower on 2017. 05. 08..
 */
public class DbConnection {

    private static final String DATABASE = "jdbc:postgresql://localhost:5432/investify";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "root";
    public static Connection CONNECTION;

    public static void dbConnect() {
        if (CONNECTION == null) {
            tryConnect();
        }
    }

    private static void tryConnect() {
        try {
            CONNECTION = DriverManager.getConnection(
                    DATABASE,
                    DB_USER,
                    DB_PASSWORD
            );
        } catch (SQLException e) {
            System.out.println("Couldn't connect to Postgres server!");

        }
    }

    public static PreparedStatement getPreparedStatement(String query) {
        dbConnect();
        PreparedStatement statement = null;

        try {
            statement = CONNECTION.prepareStatement(query);

        } catch (SQLException e) {
            System.out.println("Couldn't find user");

        } finally {
            return statement;

        }
    }
}
