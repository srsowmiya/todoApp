package com.todo;

import java.sql.SQLException;
import java.sql.Connection;
import com.todo.util.DataBaseConnection;
public class Main {
    public static void main(String[] args) throws SQLException {
        DataBaseConnection dbConnection = new DataBaseConnection();
        try {
            Connection cn= dbConnection.getDBConnection();
            System.out.println("Connection Successful");
        } catch(SQLException e) {
            System.out.println("Connection Failed");
           
        }
    
    }
}