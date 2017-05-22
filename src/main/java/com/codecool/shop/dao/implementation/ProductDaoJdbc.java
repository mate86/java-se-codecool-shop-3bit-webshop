package com.codecool.shop.dao.implementation;


import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDaoJdbc implements ProductDao {
    private static final Logger logger = LoggerFactory.getLogger(ProductDaoJdbc.class);

    private List<Product> DATA = new ArrayList<>();
    private static ProductDaoJdbc instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private ProductDaoJdbc() {
    }

    public static ProductDaoJdbc getInstance() {
        if (instance == null) {
            instance = new ProductDaoJdbc();
        }
        return instance;
    }

    @Override
    public void add(Product product) {
        logger.debug("Entering add()");
        String supplierQuery = "SELECT id FROM suppliers WHERE name ='" + product.getSupplier().getName() + "';";
        String categoryQuery = "SELECT id FROM productcategories WHERE name ='" + product.getProductCategory().getName() + "';";
        int categoryID = findId(categoryQuery);
        int supplierID = findId(supplierQuery);
        String query = "INSERT INTO products (name, defaultprice, defaultcurrency, description, productcategory, supplier) " +
                "VALUES (?, ?, ?, ?, ?, ?);";

        try {
            Connection connection = DatabaseConnection.getConnection();
            logger.debug("Database connection is OK!");
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, product.getName());
            statement.setFloat(2, product.getDefaultPrice());
            statement.setString(3, product.getDefaultCurrency().toString());
            statement.setString(4, product.getDescription());
            statement.setInt(5, categoryID);
            statement.setInt(6, supplierID);
            statement.executeUpdate();
            logger.debug("Query executed");

        } catch (SQLException e) {
            logger.error("Error! Inserting data to database failed!", e);
        }
        logger.debug("Leaving add()");
    }

    public int findId(String query) {
        logger.debug("Entering findId()");
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ) {
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                logger.debug("Leaving findId()");
                return id;
            } else {
                return 0;
            }

        } catch (SQLException e) {
            logger.error("Error! Query execution failed!", e);
        }
        logger.debug("Leaving findId()");
        return 0;
    }

    @Override
    public Product find(int id) {
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        DATA.remove(find(id));
    }

    @Override
    public List<Product> getAll() {
        logger.debug("Entering getAll()");
        String query = "SELECT * FROM products;";
        DATA = getDataFromDB(query);
        logger.debug("Query executed");
        logger.debug("Leaving getAll()");
        return DATA;
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        return DATA.stream().filter(t -> t.getSupplier().equals(supplier)).collect(Collectors.toList());
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        return DATA.stream().filter(t -> t.getProductCategory().equals(productCategory)).collect(Collectors.toList());
    }

    public List<Product> getDataFromDB(String query) {
        logger.debug("Entering getDataFromDB()");
        List<Product> data = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getConnection();
            logger.debug("Database connection is OK!");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            logger.debug("Query executed");

            while (resultSet.next()) {
                int categoryID = resultSet.getInt("productcategory");
                int supplierID = resultSet.getInt("supplier");
                Supplier supplier = getSupplierFromDB(supplierID);
                ProductCategory productCategory = getProductCategoryFromDB(categoryID);
                Product product = new Product(resultSet.getString("name"),
                        resultSet.getFloat("defaultprice"), resultSet.getString("defaultcurrency"),
                        resultSet.getString("description"), productCategory,
                        supplier);
                product.setId(resultSet.getInt("id"));
                data.add(product);
            }

        } catch (SQLException e) {
            logger.error("Error! Query execution failed!", e);
        }
        logger.debug("Leaving getDataFromDB()");
        return data;
    }

    public ProductCategory getProductCategoryFromDB(int id) {
        logger.debug("Entering getProductCategoryFromDB()");
        String query = "SELECT * FROM productcategories WHERE id=" + id + ";";
        try {
            Connection connection = DatabaseConnection.getConnection();
            logger.debug("Database connection is OK!");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            logger.debug("Query executed");

            while (resultSet.next()) {
                ProductCategory productCategory = new ProductCategory(resultSet.getString("name"),
                        resultSet.getString("description"), resultSet.getString("department"));
                logger.debug("Leaving getProductCategoryFromDB()");
                return productCategory;
            }
        } catch (SQLException e) {
            logger.error("Error! Query execution failed!", e);
        }
        logger.debug("Leaving getProductCategoryFromDB()");
        return null;
    }

    public Supplier getSupplierFromDB(int id) {
        logger.debug("Entering getSupplierFromDB()");
        String query = "SELECT * FROM suppliers WHERE id=" + id + ";";
        try {
            Connection connection = DatabaseConnection.getConnection();
            logger.debug("Database connection is OK!");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            logger.debug("Query executed");

            while (resultSet.next()) {
                Supplier supplier = new Supplier(resultSet.getString("name"), resultSet.getString("description"));
                logger.debug("Leaving getSupplierFromDB()");
                return supplier;
            }
        } catch (SQLException e) {
            logger.error("Error! Query execution failed!", e);
        }
        logger.debug("Leaving getSupplierFromDB()");
        return null;
    }
}
