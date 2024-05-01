package controllers;

import views.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableModel;
import models.*;
import java.util.List;
import views.SearchView;
import java.io.File;
import java.io.PrintWriter;
import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;

public class ContactController {

    private ContactView contactView;
    private SearchView searchView;
    private SearchController searchController;

    public ContactController(ContactView cv) {
        this.contactView = cv;
        this.searchView = new SearchView();
        this.searchController = new SearchController(searchView, contactView);
        searchController.setupHighlighting();
        updateContactList();

        contactView.addAddButtonListener(e -> {
            Contact contact = new Contact();
            contact.setFirstname(contactView.getFirstName());
            contact.setLastname(contactView.getLastName());
            contact.setPhoneNumber(contactView.getPhoneNumber());
            contact.setEmail(contactView.getEMail());

            ContactDataAccess contactData = new ContactDataAccess();
            if (contactData.addContact(contact)) {
                JOptionPane.showMessageDialog(null, "Contact added successfully");
                updateContactList();
            } else {
                JOptionPane.showMessageDialog(null, "An error occurred");
            }
        });

        contactView.addUpdateButtonListener(e -> {
            Contact contact = getSelectedContact();
            if (contact == null) {
                JOptionPane.showMessageDialog(null, "No contact selected.");
                return;
            }
            contact.setFirstname(contactView.getFirstName());
            contact.setLastname(contactView.getLastName());
            contact.setPhoneNumber(contactView.getPhoneNumber());
            contact.setEmail(contactView.getEMail());

            if (new ContactDataAccess().updateContact(contact)) {
                JOptionPane.showMessageDialog(null, "Contact updated successfully");
                updateContactList();  // Refresh the list
            } else {
                JOptionPane.showMessageDialog(null, "Update failed. Please try again.");
            }
        });

        contactView.addDeleteButtonListener(e -> {
            Contact selectedContact = getSelectedContact();
            if (selectedContact == null) {
                JOptionPane.showMessageDialog(null, "No contact selected.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                contactView,
                "Are you sure you want to delete this contact?",
                "Delete Contact",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                ContactDataAccess contactData = new ContactDataAccess();
                if (contactData.deleteContact(selectedContact.getId())) {
                    JOptionPane.showMessageDialog(null, "Contact deleted successfully");
                    updateContactList(); // Refresh the list after deletion
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to delete contact");
                }
            }
        });

        contactView.addLogoutButtonListener(e -> {
            UserDataAccess.currentUserId = 0;
            contactView.setVisible(false);
            LoginView lv = new LoginView();
            lv.setVisible(true);
            new LoginController(lv);
        });

        contactView.getContactTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Contact contact = getSelectedContact();
                if (contact != null) {
                    updateFields(contact);
                }
            }
        });
        
        contactView.addSearchButtonListener(e -> {
            searchView.setVisible(true);
        });
        
        contactView.addExportButtonListener(e -> {
            exportContacts();
        });
        
    }

    private void updateContactList() {
        ContactDataAccess data = new ContactDataAccess();
        List<Contact> contacts = data.getContacts();
        contactView.setContactsToModel(contacts);
    }

    private Contact getSelectedContact() {
        int viewRow = contactView.getContactTable().getSelectedRow();
        if (viewRow != -1) {
            int modelRow = contactView.getContactTable().convertRowIndexToModel(viewRow);
            List<Contact> contacts = new ContactDataAccess().getContacts();
            if (modelRow < contacts.size()) {
                return contacts.get(modelRow);
            }
        }
        return null;
    }

    private void updateFields(Contact contact) {
        contactView.getFirstNameField().setText(contact.getFirstname());
        contactView.getLastNameField().setText(contact.getLastname());
        contactView.getPhoneNumberField().setText(contact.getPhoneNumber());
        contactView.getEmailField().setText(contact.getEmail());
    }
    
    private void exportContacts() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");
        int userSelection = fileChooser.showSaveDialog(contactView);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (PrintWriter pw = new PrintWriter(fileToSave)) {
                TableModel model = contactView.getContactTable().getModel();
                for (int i = 0; i < model.getRowCount(); i++) {
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j < model.getColumnCount(); j++) { // from ChatGPT
                        sb.append(model.getValueAt(i, j).toString());
                        if (j < model.getColumnCount() - 1) sb.append(",");
                    }
                    pw.println(sb.toString());
                }
                JOptionPane.showMessageDialog(contactView, "Data exported successfully to " + fileToSave.getAbsolutePath());

                // Ask if the user wants to email the file - from ChatGPT
                if (JOptionPane.showConfirmDialog(contactView, "Do you want to email this file? Please attach manually.", "Email File", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        String mailto = "mailto:?subject=Exported%20Contacts&body=File%20saved%20at%20" + URLEncoder.encode(fileToSave.getAbsolutePath(), "UTF-8");
                        URI uri = URI.create(mailto);
                        desktop.mail(uri);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(contactView, "Error exporting data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
