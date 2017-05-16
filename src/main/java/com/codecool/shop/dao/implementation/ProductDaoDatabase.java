package com.codecool.shop.dao.implementation;


import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDaoDatabase implements ProductDao {

    private List<Product> DATA = new ArrayList<>();
    private static ProductDaoDatabase instance = null;

    private static final String DATABASE = "jdbc:postgresql://localhost:5432/codecoolshop";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    /* A private Constructor prevents any other class from instantiating.
     */
    private ProductDaoDatabase() {
    }

    public static ProductDaoDatabase getInstance() {
        if (instance == null) {
            instance = new ProductDaoDatabase();
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

//        String query = "INSERT INTO products (name, defaultprice, defaultcurrency, description, productcategory, supplier) " +
//                "VALUES ('" + product.getName() + "', " + product.getDefaultPrice() + ", '" + product.getDefaultCurrency().toString() + "', ?, " + categoryID + ", " +
//                supplierID + ");";


        try (Connection connection = getConnection();
//             Statement statement = connection.createStatement();
             PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, product.getName());
            statement.setFloat(2, product.getDefaultPrice());
            statement.setString(3, product.getDefaultCurrency().toString());
            statement.setString(4, product.getDescription());
            statement.setInt(5, categoryID);
            statement.setInt(6, supplierID);
//            statement.execute(query);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
//        executeQuery(query);
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

//    private void executeQuery(String query) {
//        try (Connection connection = getConnection();
//             Statement statement = connection.createStatement();
//        ) {
//            statement.execute(query);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
