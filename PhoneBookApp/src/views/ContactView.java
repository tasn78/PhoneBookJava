package views;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import models.*;

public class ContactView extends JFrame {
	
    private JTextField txtFirstname, txtLastname, txtPhoneNumber, txtEmail;
    private JButton btnAdd, btnUpdate, btnDelete, btnSearch, btnLogout, btnExport;
    private JTable contactTable;
    private DefaultTableModel tableModel;
    private JLabel labelContactsList;
    
    public ContactView() {
        setTitle("Phonebook");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        initializeComponents();
        layoutComponents();
        pack();
        setLocationRelativeTo(null);
    }
    
    // Initializes all components of the Contact View
    private void initializeComponents() {
        txtFirstname = new JTextField(20);
        txtLastname = new JTextField(20);
        txtPhoneNumber = new JTextField(20);
        txtEmail = new JTextField(20);
        
        btnAdd = new JButton("Add");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 12));
        btnUpdate = new JButton("Update");
        btnUpdate.setFont(new Font("Arial", Font.BOLD, 12));
        btnDelete = new JButton("Delete");
        btnDelete.setFont(new Font("Arial", Font.BOLD, 12));
        btnSearch = new JButton("Search");
        btnSearch.setFont(new Font("Arial", Font.BOLD, 12));
        btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Arial", Font.BOLD, 12));
        btnExport = new JButton("Export");
        btnLogout.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Set color for buttons - from HTML Color Codes [3]
        btnAdd.setBackground(new Color(0, 150, 255)); // light blue
        btnAdd.setForeground(Color.WHITE);
        btnUpdate.setBackground(new Color(0, 163, 108)); // jade
        btnUpdate.setForeground(Color.WHITE);
        btnDelete.setBackground(new Color(255, 0, 73)); // red
        btnDelete.setForeground(Color.BLACK);
        btnSearch.setBackground(new Color(243, 246, 4)); // yellow
        btnSearch.setForeground(Color.BLACK);
        btnLogout.setBackground(new Color(4, 60, 246)); // blue
        btnLogout.setForeground(Color.WHITE);
        btnExport.setBackground(Color.BLACK);
        btnExport.setForeground(Color.WHITE);
        
        // Set all buttons to opaque to show background color
        btnAdd.setOpaque(true);
        btnAdd.setBorderPainted(true);
        btnUpdate.setOpaque(true);
        btnUpdate.setBorderPainted(true);
        btnDelete.setOpaque(true);
        btnDelete.setBorderPainted(true);
        btnSearch.setOpaque(true);
        btnSearch.setBorderPainted(true);
        btnLogout.setOpaque(true);
        btnLogout.setBorderPainted(true);
        btnExport.setOpaque(true);
        btnExport.setBorderPainted(true);
        
        // Initialize table model and table - from Oracle [2]
        tableModel = new DefaultTableModel(new Object[]{"First name", "Last name", "Phone Number", "E-mail"}, 0);
        contactTable = new JTable(tableModel);
        contactTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Set up a cell renderer to center text in each cell from ChatGPT [1]
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        contactTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        contactTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        contactTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        contactTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        
        // Initialize the label for the contacts list title
        labelContactsList = new JLabel("Contacts List");
        labelContactsList.setFont(new Font("Arial", Font.BOLD, 18));
        labelContactsList.setHorizontalAlignment(JLabel.CENTER);
        labelContactsList.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));  // Adds space above the title
    }

    private void layoutComponents() {
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("    First name:"));
        inputPanel.add(txtFirstname);
        inputPanel.add(new JLabel("    Last name:"));
        inputPanel.add(txtLastname);
        inputPanel.add(new JLabel("    Phone number:"));
        inputPanel.add(txtPhoneNumber);
        inputPanel.add(new JLabel("    Email:"));
        inputPanel.add(txtEmail);
        inputPanel.add(btnAdd);
        inputPanel.add(btnUpdate);

        JScrollPane scrollPane = new JScrollPane(contactTable);
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(Color.WHITE);
        listPanel.add(labelContactsList, BorderLayout.NORTH);
        listPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnExport);
        buttonPanel.add(btnLogout);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(listPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
    }
    
    // Contact table from Oracle [2]
    public JTable getContactTable() {
        return contactTable;
    }
    
    // Add button listener to add button
    public void addAddButtonListener(ActionListener listener) {
        btnAdd.addActionListener(listener);
    }
    
    // Add update button listener to update button
    public void addUpdateButtonListener(ActionListener listener) {
        btnUpdate.addActionListener(listener);
    }
    
    // Add delete button listener to delete button
    public void addDeleteButtonListener(ActionListener listener) {
        btnDelete.addActionListener(listener);
    }
    
    // Add search button listener to search button
    public void addSearchButtonListener(ActionListener listener) {
        btnSearch.addActionListener(listener);
    }
    
    // Add lougout button listener to logout button
    public void addLogoutButtonListener(ActionListener listener) {
        btnLogout.addActionListener(listener);
    }
    
    // Export button listener to export button
    public void addExportButtonListener(ActionListener listener) {
        btnExport.addActionListener(listener);
    }

    // Getters and setters
    public JTextField getFirstNameField() {
        return txtFirstname;
    }

    public String getFirstName() {
        return getFirstNameField().getText();
    }

    public JTextField getLastNameField() {
        return txtLastname;
    }

    public String getLastName() {
        return getLastNameField().getText();
    }

    public JTextField getPhoneNumberField() {
        return txtPhoneNumber;
    }

    public String getPhoneNumber() {
        return getPhoneNumberField().getText();
    }
    
    public JTextField getEmailField() {
        return txtEmail;
    }

    public String getEMail() {
        return getEmailField().getText();
    }
    
    // Sets contacts to a table - from Oracle [2]
    public void setContactsToModel(List<Contact> contacts) {
        tableModel.setRowCount(0); // Clear existing data
        for (Contact c : contacts) {
            tableModel.addRow(new Object[]{c.getFirstname(), c.getLastname(), c.getPhoneNumber(), c.getEmail()});
        }
    }
}