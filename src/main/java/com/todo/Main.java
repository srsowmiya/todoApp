package com.todo;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.todo.gui.TodoAppGUI;
import com.todo.util.DataBaseConnection;
public class Main {
    public static void main(String[] args) throws SQLException {
        DataBaseConnection dbConnection = new DataBaseConnection();
        try {
            Connection cn= dbConnection.getDBConnection();
            System.out.println("Connection Successful");
        } catch(SQLException e) {
            System.out.println("Connection Failed");
           System.exit(1); //process is terminated
        }

        try{
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(ClassNotFoundException | InstantiationException | IllegalAccessException| UnsupportedLookAndFeelException e){
           System.err.println("Could not set Look and Feel"+e.getMessage()); 
        }

        SwingUtilities.invokeLater(() -> {  //invoke later creates a new thread (eventdispatch thread)
            try {
                new TodoAppGUI().setVisible(true); 
            } catch (Exception e) {
                System.err.println("Error starting the application"+e.getLocalizedMessage());
            }
           
           
        });
    
    }
}