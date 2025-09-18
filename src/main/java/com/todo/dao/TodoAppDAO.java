package com.todo.dao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;

import java.sql.Timestamp;      
import java.sql.Statement;

import com.todo.model.Todo;
import com.todo.util.DataBaseConnection;

public class TodoAppDAO {
    private static final String SELECT_ALL_TODOS = "SELECT * FROM todos ORDER BY created_at DESC";
    private static final String INSERT_TODO = "INSERT INTO todos (title, description, completed, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";

    //create new todo
    public int createTodo(Todo todo)  throws SQLException{
        try(
            Connection conn=  DataBaseConnection.getDBConnection();
            PreparedStatement stmt= conn.prepareStatement(INSERT_TODO, Statement.RETURN_GENERATED_KEYS);
        )
        {
            stmt.setString(1, todo.getTitle());
            stmt.setString(2, todo.getDescription());
            stmt.setBoolean(3, todo.isCompleted());
            stmt.setTimestamp(4, Timestamp.valueOf(todo.getCreated_at()));
            stmt.setTimestamp(5, Timestamp.valueOf(todo.getUpdated_at()));           

            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0){
                throw new SQLException("Creating todo failed, no rows affected.");
            }

            try(ResultSet generatedKeys = stmt.getGeneratedKeys();){
                if(generatedKeys.next()){
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating todo failed, no ID obtained.");
                }
            }
        } catch(SQLException e) {
            System.err.println("Error creating todo: "+e.getMessage());
            throw e; // rethrow the exception after logging
        }
    }

    private Todo getTodoRow(ResultSet rs) throws SQLException{
        int id = rs.getInt("id");
        String title= rs.getString("title");
        String description =rs.getString("description");
        boolean completed= rs.getBoolean("completed");
        LocalDateTime created_at = rs.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updated_at = rs.getTimestamp("updated_at").toLocalDateTime();

        Todo todo=new Todo(id,title,description,completed,created_at,updated_at);
        return todo;

    }
    public List<Todo> getAllTodos() throws SQLException {
        // Implement database retrieval logic here
        List<Todo> todos=new ArrayList<>();

        try (Connection conn= DataBaseConnection.getDBConnection();
        PreparedStatement stmt=conn.prepareStatement(SELECT_ALL_TODOS);
        ResultSet res=stmt.executeQuery();)
        {
            while(res.next())
            {
                todos.add(getTodoRow(res));
            }
        } catch(SQLException e) {
            System.err.println("Error fetching todos: "+e.getMessage());
            throw e; // rethrow the exception after logging

        }
    

        return todos;
    }

}