package com.codecool.shop.model;

import com.codecool.shop.interfaces.SessionReady;
import org.eclipse.jetty.websocket.common.events.ParamList;
import spark.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by flowerpower on 2017. 05. 07..
 */
public class User extends PsqlObject implements SessionReady {

    private String username;
    private String password;
    private String email;
    private ArrayList shareholds;
    private int cash;
    private int isActive;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.shareholds = new ArrayList();
    }

    public void saveToSession(Session session) {
        session.attribute("user", this);
    }

    public void initFromSession(Session session) {
        session.attribute("user");
    }

    public static User auth(String user, String password) {
        String query = "SELECT * FROM users WHERE username LIKE ? and password LIKE ?;";
        ResultSet resultSet;

        try {
            PreparedStatement statement = getPreparedStatement(query);
            statement.setString(1, user);
            statement.setString(2, password);
            statement.execute();
            resultSet = statement.getResultSet();

            if (resultSet.next()) {
                return new User(
                        resultSet.getString("username"),
                        resultSet.getString("password")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

        return new User("notfound","notfound");
    }

    public static User find(Integer id) {
        String query = "SELECT * FROM users WHERE id = ?;";
        PreparedStatement statement = getPreparedStatement(query);
        ResultSet resultSet;
        User result = new User("notfound","notfound");

        try {
            statement.setString(1, id.toString());
            resultSet = statement.executeQuery();


            if (resultSet.next()) {
                result = new User(
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("cash"),
                        resultSet.getString("cash")
                );
            }

        } catch (SQLException e) {

        }

        return result;

    }

    // *** GETTERS & SETTERS
    public String getUsername() {
        return username;
    }
}
