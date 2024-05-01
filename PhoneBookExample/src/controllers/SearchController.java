package controllers;

import views.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class SearchController {
    private SearchView searchView;
    private ContactView contactView;
    private String currentSearchText = "";

    public SearchController(SearchView searchView, ContactView contactView) {
        this.searchView = searchView;
        this.contactView = contactView;
        setupController();
        setupHighlighting();
    }

    private void setupController() {
        searchView.addSearchButtonListener(e -> performSearch());
    }

    private void performSearch() {
        currentSearchText = searchView.getSearchText().trim().toLowerCase();
        contactView.getContactTable().repaint();
    }
    
    // Finds row with matching search
    private boolean isRowMatchingSearchCriteria(JTable table, int row) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            Object cellValue = table.getValueAt(row, i);
            if (cellValue != null && cellValue.toString().toLowerCase().contains(currentSearchText)) {
                return true;
            }
        }
        return false;
    }
    
    // Highlights search results - from ChatGPT
    public class HighlightRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!currentSearchText.isEmpty() && isRowMatchingSearchCriteria(table, row)) {
                c.setBackground(Color.GREEN); // Highlight the entire row if any cell matches
            } else {
                c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            }
            return c;
        }
    }

    public void setupHighlighting() {
        JTable table = contactView.getContactTable();
        HighlightRenderer highlightRenderer = new HighlightRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(highlightRenderer);
        }
    }
}