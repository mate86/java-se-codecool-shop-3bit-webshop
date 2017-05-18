package com.codecool.shop.dao.implementation;


import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDaoJdbc implements ProductDao {

    private List<Product> DATA = new ArrayList<>();
    private static ProductDaoJdbc instance = null;

    private static final String DATABASE = "jdbc:postgresql://localhost:5432/codecoolshop";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

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
        String supplierQuery = "SELECT id FROM suppliers WHERE name ='" + product.getSupplier().getName() + "';";
        String categoryQuery = "SELECT id FROM productcategories WHERE name ='" + product.getProductCategory().getName() + "';";
        int categoryID = findId(categoryQuery);
        int supplierID = findId(supplierQuery);
        String query = "INSERT INTO products (name, defaultprice, defaultcurrency, description, productcategory, supplier) " +
                "VALUES (?, ?, ?, ?, ?, ?);";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, product.getName());
            statement.setFloat(2, product.getDefaultPrice());
            statement.setString(3, product.getDefaultCurrency().toString());
            statement.setString(4, product.getDescription());
            statement.setInt(5, categoryID);
            statement.setInt(6, supplierID);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int findId(String query) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ) {
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                return id;
            } else {
                return 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        String query = "SELECT * FROM products;";
        DATA = getDataFromDB(query);
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

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DATABASE,
                DB_USER,
                DB_PASSWORD);
    }

    public List<Product> getDataFromDB(String query) {
        List<Product> data = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ) {
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
            e.printStackTrace();
        }
        return data;
    }

    public ProductCategory getProductCategoryFromDB(int id) {
        String query = "SELECT * FROM productcategories WHERE id=" + id + ";";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ) {
            while (resultSet.next()) {
                ProductCategory productCategory = new ProductCategory(resultSet.getString("name"),
                        resultSet.getString("description"), resultSet.getString("department"));
                return productCategory;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Supplier getSupplierFromDB(int id) {
        String query = "SELECT * FROM suppliers WHERE id=" + id + ";";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ) {
            while (resultSet.next()) {
                Supplier supplier = new Supplier(resultSet.getString("name"), resultSet.getString("description"));
                return supplier;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
