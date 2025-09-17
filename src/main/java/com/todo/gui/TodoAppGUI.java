package com.todo.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.todo.dao.TodoAppDAO;  

public class TodoAppGUI extends JFrame // for representing the main window
{
    private TodoAppDAO todoDAO;
    private JTable todoTable;
    private DefaultTableModel tableModel;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JCheckBox completedCheckBox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JComboBox<String> filterComboBox;


    public TodoAppGUI() {
        this.todoDAO = new TodoAppDAO();
        initializeComponents();
        setupLayout();
        
    }

    private void initializeComponents() {
        setTitle("Todo Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); //decides the position of the window ,null is centers

            
        String [] columnNames = {"ID", "Title", "Description", "Completed", "Created At","Updated At"};
        tableModel=new DefaultTableModel(columnNames,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        todoTable = new JTable(tableModel);
        todoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        todoTable.getSelectionModel().addListSelectionListener(
            e -> {
                if(!e.getValueIsAdjusting()){
                    //loadSelectedTodo();
                }
            }
        );
        
        titleField=new JTextField(20);
        descriptionArea=new JTextArea(3,20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);


        completedCheckBox=new JCheckBox("completed");

        addButton=new JButton("Add todo");
        updateButton=new JButton("Update todo");
        deleteButton=new JButton("Delete todo");
        refreshButton=new JButton("Refresh");

        String[] filterOptions={"All","Completed","Pending"};
        filterComboBox=new JComboBox<>(filterOptions);
        filterComboBox.addActionListener(e -> {
           //filterTodos();
        });
    }
    private void setupLayout(){   //panel to group related components
        setLayout(new BorderLayout());
        JPanel inputPanel=new JPanel(new GridBagLayout()); 
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.insets=new Insets(5,5,5,5); //padding around components


        gbc.gridx=0;
        gbc.gridy=0;
        gbc.anchor=GridBagConstraints.WEST;
        inputPanel.add(new JLabel("Title"),gbc);

        gbc.gridx=1;
        inputPanel.add(titleField,gbc);
       
        gbc.gridy=1;
        gbc.gridx=0;
        inputPanel.add(new JLabel("Description"),gbc);
        gbc.gridx=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        inputPanel.add(new JScrollPane(descriptionArea),gbc);

        gbc.gridy=2;
        gbc.gridx=1;
        inputPanel.add(completedCheckBox,gbc);

        JPanel buttonPanel=new JPanel(new FlowLayout());  //places items from left to right then moves to the next line
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        JPanel filterPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter:"));
        filterPanel.add(filterComboBox);

        JPanel northPanel=new JPanel(new BorderLayout());
        northPanel.add(inputPanel,BorderLayout.CENTER);
        northPanel.add(buttonPanel,BorderLayout.SOUTH);
        northPanel.add(filterPanel,BorderLayout.NORTH);


        add(northPanel,BorderLayout.NORTH);

        add(new JScrollPane(todoTable),BorderLayout.CENTER);

        JPanel statusPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Select a todo to edit or delete"));
        add(statusPanel,BorderLayout.SOUTH);
    }
}
