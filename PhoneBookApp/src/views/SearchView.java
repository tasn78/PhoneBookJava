package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

// View to allow user to search their contact list
public class SearchView extends JFrame {
    private JTextField searchText;
    private JButton searchButton;

    public SearchView() {
        setTitle("Search Contacts");
        setSize(300, 100);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        initializeComponents();
        layoutComponents();
        setLocationRelativeTo(null);
    }
    
    // Initializes search field
    private void initializeComponents() {
        searchText = new JTextField(15);
        searchButton = new JButton("Search");
    }
    
    // Creates layout
    private void layoutComponents() {
        setLayout(new FlowLayout());
        add(new JLabel("Enter search query:"));
        add(searchText);
        add(searchButton);
    }
    
    // Search button
    public void addSearchButtonListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }
    
    // Getter for search text
    public String getSearchText() {
        return searchText.getText();
    }
}