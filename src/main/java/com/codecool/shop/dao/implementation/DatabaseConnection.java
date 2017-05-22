package com.codecool.shop.dao.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class DatabaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    private static String DATABASE;
    private static String DB_USER;
    private static String DB_PASSWORD;
    private static String initFilePath = "src/main/resources/connection.properties";

    public static Connection getConnection() throws SQLException {
        logger.debug("Entering getConnection()");
        connectionAuthentication();
        logger.debug("Authentication parameters are not null: DATABASE: {}, DB_USER: {}, DB_PASSWORD: {}",
                !DATABASE.equals(null), !DB_USER.equals(null), !DB_PASSWORD.equals(null));
        logger.debug("Leaving getConnection()");
        return DriverManager.getConnection(
                DATABASE,
                DB_USER,
                DB_PASSWORD);
    }

    private static void connectionAuthentication() {
        logger.debug("Entering connectionAuthentication()");
        logger.debug("Authentication file path: {}", initFilePath);
        ArrayList<String> fileLines = new ArrayList<>();
        try {
            Scanner s = new Scanner(new File(initFilePath));
            logger.debug("Opening authentication file is successful!");
            while (s.hasNext()) {
                fileLines.add(s.next());
            }
            logger.debug("Reading authentication file is successful!");
            logger.debug("Number of readed file lines: {}", fileLines.size());
            s.close();
            logger.debug("Closing authentication file is successful!");
        } catch (FileNotFoundException e) {
            logger.error("ERROR! Database authentication file is missing!", e);
//            e.printStackTrace();
        }
        DATABASE = "jdbc:postgresql://" + fileLines.get(0) + "/" + fileLines.get(1);
        DB_USER = fileLines.get(2);
        DB_PASSWORD = fileLines.get(3);
        logger.debug("Leaving connectionAuthentication()");
    }
}
