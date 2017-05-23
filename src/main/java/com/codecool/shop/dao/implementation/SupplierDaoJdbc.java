package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class SupplierDaoJdbc implements SupplierDao {
    private static final Logger logger = LoggerFactory.getLogger(SupplierDaoJdbc.class);

    private List<Supplier> DATA = new ArrayList<>();
    private static SupplierDaoJdbc instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private SupplierDaoJdbc() {
    }

    public static SupplierDaoJdbc getInstance() {
        if (instance == null) {
            instance = new SupplierDaoJdbc();
        }
        return instance;
    }

    @Override
    public void add(Supplier supplier) {
        logger.debug("Entering add()");
        String query = "INSERT INTO suppliers (name, description) " +
                "VALUES ('" + supplier.getName() + "', '" + supplier.getDescription() + "');";
        executeQuery(query);
        logger.debug("Query executed");
        logger.debug("Leaving add()");
    }

    @Override
    public Supplier find(int id) {
        logger.debug("Finding supplier by id: {}", id);
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        logger.debug("Removing supplier by id: {}", id);
        DATA.remove(find(id));
    }

    @Override
    public List<Supplier> getAll() {
        logger.debug("Entering getAll()");
        String query = "SELECT * FROM suppliers;";
        DATA = getDataFromDB(query);
        logger.debug("Query executed");
        logger.debug("Leaving getAll()");
        return DATA;
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

    public List<Supplier> getDataFromDB(String query) {
        logger.debug("Entering getDataFromDB()");
        List<Supplier> data = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getConnection();
            logger.debug("Database connection is OK!");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            logger.debug("Query executed");

            while (resultSet.next()) {
                Supplier supplier = new Supplier(resultSet.getString("name"),
                        resultSet.getString("description"));
                supplier.setId(resultSet.getInt("id"));
                data.add(supplier);
            }

        } catch (SQLException e) {
            logger.error("Error! Getting data from database failed!", e);
        }
        logger.debug("Leaving getDataFromDB()");
        return data;
    }
}
