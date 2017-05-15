package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class SupplierDaoDatabase implements SupplierDao {

    private List<Supplier> DATA = new ArrayList<>();
    private static SupplierDaoDatabase instance = null;

    private static final String DATABASE = "jdbc:postgresql://localhost:5432/codecoolshop";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    /* A private Constructor prevents any other class from instantiating.
     */
    private SupplierDaoDatabase() {
    }

    public static SupplierDaoDatabase getInstance() {
        if (instance == null) {
            instance = new SupplierDaoDatabase();
        }
        return instance;
    }

    @Override
    public void add(Supplier supplier) {
        String query = "INSERT INTO suppliers (name, description) " +
                "VALUES ('" + supplier.getName() + "', '" + supplier.getDescription() + "');";
        executeQuery(query);


//        supplier.setId(DATA.size() + 1);
//        DATA.add(supplier);
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
