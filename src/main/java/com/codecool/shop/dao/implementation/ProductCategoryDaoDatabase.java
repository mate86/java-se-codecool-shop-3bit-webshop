package com.codecool.shop.dao.implementation;


import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoDatabase implements ProductCategoryDao {

    private List<ProductCategory> DATA = new ArrayList<>();
    private static ProductCategoryDaoDatabase instance = null;

    private static final String DATABASE = "jdbc:postgresql://localhost:5432/codecoolshop";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    /* A private Constructor prevents any other class from instantiating.
     */
    private ProductCategoryDaoDatabase() {
    }

    public static ProductCategoryDaoDatabase getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoDatabase();
        }
        return instance;
    }

    @Override
    public void add(ProductCategory category) {
        String query = "INSERT INTO productcategories (name, department, description) " +
                "VALUES ('" + category.getName() + "', '" + category.getDepartment() + "', '" + category.getDescription() + "');";
        executeQuery(query);
    }

    @Override
    public ProductCategory find(int id) {
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        DATA.remove(find(id));
    }

    @Override
    public List<ProductCategory> getAll() {
        return DATA;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DATABASE,
                DB_USER,
                DB_PASSWORD);
    }

    private void executeQuery(String query) {
        try (Connection connection = getConnection();
             Statement statement =connection.createStatement();
        ){
            statement.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
