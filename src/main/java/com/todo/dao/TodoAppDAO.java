package com.todo.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.todo.model.Todo;
import com.todo.util.DataBaseConnection;

public class TodoAppDAO {
    private static final String SELECT_ALL_TODOS = "SELECT * FROM todos ORDER BY created_at DESC";
    private static final String INSERT_TODO = "INSERT INTO todos (title, description, completed, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_TODO = "DELETE FROM todos WHERE id = ?";
    private static final String SELECT_TODO_BY_ID = "SELECT * FROM todos WHERE id = ?";
    private static final String UPDATE_TODO = "UPDATE todos SET title = ?, description = ?, completed = ?, updated_at = ? WHERE id = ?";
    

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

      public Todo getTodoById(int todoId) throws SQLException {
        try (
            Connection conn = DataBaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(SELECT_TODO_BY_ID)
        ) {
            stmt.setInt(1, todoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return getTodoRow(rs);
                }
            }
        }
        return null; // no todo found
    }

    public boolean updateTodo(Todo todo) throws SQLException {
        try (
            Connection conn = DataBaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_TODO)
        ) {
            stmt.setString(1, todo.getTitle());
            stmt.setString(2, todo.getDescription());
            stmt.setBoolean(3, todo.isCompleted());
            stmt.setTimestamp(4, java.sql.Timestamp.valueOf(LocalDateTime.now())); // update time
            stmt.setInt(5, todo.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteTodo(int todoId) throws SQLException {
        try (
            Connection conn = DataBaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(DELETE_TODO)
        ) {
            stmt.setInt(1, todoId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
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