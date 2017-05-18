package com.codecool.shop.dao.implementation;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class DatabaseConnection {
    private static String DATABASE;
    private static String DB_USER;
    private static String DB_PASSWORD;
    private static String initFilePath = "src/main/resources/connection.properties";

    public static Connection getConnection() throws SQLException {
        connectionAuthentication();
        return DriverManager.getConnection(
                DATABASE,
                DB_USER,
                DB_PASSWORD);
    }

    private static void connectionAuthentication() {
        ArrayList<String> fileLines = new ArrayList<>();
        try {
            Scanner s = new Scanner(new File(initFilePath));
            while (s.hasNext()) {
                fileLines.add(s.next());
            }
            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        DATABASE = "jdbc:postgresql://" + fileLines.get(0) + "/" + fileLines.get(1);
        DB_USER = fileLines.get(2);
        DB_PASSWORD = fileLines.get(3);
    }
}
