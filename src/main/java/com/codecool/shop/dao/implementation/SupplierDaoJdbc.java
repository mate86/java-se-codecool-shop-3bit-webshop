package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class SupplierDaoJdbc implements SupplierDao {

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
        String query = "INSERT INTO suppliers (name, description) " +
                "VALUES ('" + supplier.getName() + "', '" + supplier.getDescription() + "');";
        executeQuery(query);
    }

    @Override
    public Supplier find(int id) {
        return DATA.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        DATA.remove(find(id));
    }

    @Override
    public List<Supplier> getAll() {
        String query = "SELECT * FROM suppliers;";
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

    public List<Supplier> getDataFromDB(String query) {
        List<Supplier> data = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
        ) {
            while (resultSet.next()) {
                Supplier supplier = new Supplier(resultSet.getString("name"),
                        resultSet.getString("description"));
                supplier.setId(resultSet.getInt("id"));
                data.add(supplier);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
