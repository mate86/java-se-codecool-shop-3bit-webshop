package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.StatusDao;
import com.codecool.shop.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class StatusDaoJdbc implements StatusDao {
    private static final Logger logger = LoggerFactory.getLogger(StatusDaoJdbc.class);

    private static StatusDaoJdbc instance = null;

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
        logger.debug("Entering add()");
        String query = "INSERT INTO statuses (name) VALUES ('"+name+"');";
        executeQuery(query);
        logger.debug("Query executed");
        logger.debug("Leaving add()");
    }

    public Status find(int id) {
        logger.debug("Entering find()");
        String query = "SELECT * FROM statuses WHERE id = "+id+";";
        logger.debug("Leaving find()");
        return getDataFromDB(query);
    }

    public void remove(int id) {
        logger.debug("Entering remove()");
        String query = "DELETE FROM status WHERE id = "+id+";";
        executeQuery(query);
        logger.debug("Query executed");
        logger.debug("Leaving remove()");
    }

    private void executeQuery(String query) {
        logger.debug("Entering executeQuery()");
        try {
            Connection connection = DatabaseConnection.getConnection();
            logger.debug("Database connection is OK!");
            Statement statement = connection.createStatement();

            statement.execute(query);
            logger.debug("Query executed");

        } catch (SQLException e) {
            logger.error("Error! Query execution failed!", e);
        }
        logger.debug("Leaving executeQuery()");
    }

    public Status getDataFromDB(String query) {
        logger.debug("Entering getDataFromDB()");

        try {
            Connection connection = DatabaseConnection.getConnection();
            logger.debug("Database connection is OK!");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            logger.debug("Query executed");

            while (resultSet.next()) {
                logger.debug("Leaving getDataFromDB()");
                return new Status(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );
            }

        } catch (SQLException e) {
            logger.error("Error! Getting data from database failed!", e);
        }
        logger.debug("Leaving getDataFromDB()");
        return new Status();
    }
}
