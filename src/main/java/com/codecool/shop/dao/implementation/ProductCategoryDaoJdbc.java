package com.codecool.shop.dao.implementation;


import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.ProductCategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoJdbc implements ProductCategoryDao {

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
        String query = "SELECT * FROM productcategories;";
        DATA = getDataFromDB(query);
        return DATA;
    }

    private void executeQuery(String query) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
        ) {
            statement.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ProductCategory> getDataFromDB(String query) {
        List<ProductCategory> data = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ) {
            while (resultSet.next()) {
                ProductCategory productCategory = new ProductCategory(resultSet.getString("name"),
                        resultSet.getString("description"), resultSet.getString("department"));
                productCategory.setId(resultSet.getInt("id"));
                data.add(productCategory);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
