package com.codecool.shop.dao.implementation;


import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoJdbc implements ProductCategoryDao {
    private static final Logger logger = LoggerFactory.getLogger(ProductCategoryDaoJdbc.class);

    private List<ProductCategory> DATA = new ArrayList<>();
    private static ProductCategoryDaoJdbc instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private ProductCategoryDaoJdbc() {
    }

    public static ProductCategoryDaoJdbc getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoJdbc();
        }
        return instance;
    }

    @Override
    public void add(ProductCategory category) {
        logger.debug("Entering add()");
        String query = "INSERT INTO productcategories (name, department, description) " +
                "VALUES ('" + category.getName() + "', '" + category.getDepartment() + "', '" + category.getDescription() + "');";
        executeQuery(query);
        logger.debug("Query executed");
        logger.debug("Leaving add()");
    }

    @Override
    public ProductCategory find(int id) {
        logger.debug("Entering find()");
        logger.debug("Leaving find()");
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id){
        logger.debug("Entering remove()");
        DATA.remove(find(id));
        logger.debug("Product category removed");
        logger.debug("Leaving remove()");
    }

    @Override
    public List<ProductCategory> getAll() {
        logger.debug("Entering getAll()");
        String query = "SELECT * FROM productcategories;";
        DATA = getDataFromDB(query);
        logger.debug("Leaving getAll()");
        return DATA;
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

    public List<ProductCategory> getDataFromDB(String query) {
        logger.debug("Entering getDataFromDB()");
        List<ProductCategory> data = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getConnection();
            logger.debug("Database connection is OK!");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                ProductCategory productCategory = new ProductCategory(resultSet.getString("name"),
                        resultSet.getString("description"), resultSet.getString("department"));
                productCategory.setId(resultSet.getInt("id"));
                data.add(productCategory);
            }

        } catch (SQLException e) {
            logger.error("Error! Getting data from database failed!", e);
        }
        logger.debug("Number of product category: {}", data.size());
        logger.debug("Leaving getDataFromDB()");
        return data;
    }
}
