package controllers;

import views.*;
import java.awt.event.*;

import javax.mail.MessagingException;
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
import javax.mail.*;
import javax.mail.internet.*;

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
        
        // Add contact button listener
        contactView.addAddButtonListener(e -> {
        	String firstName = contactView.getFirstName().trim();
            String lastName = contactView.getLastName().trim();
            String phoneNumber = contactView.getPhoneNumber().trim();
            String email = contactView.getEMail().trim();

            // Validate input before adding contact
            if (firstName.isEmpty() || lastName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in the required fields (First name, Last name).", "Missing Information", JOptionPane.ERROR_MESSAGE);
                return; // Stop add process
            }

            // Create contact object after validation
            Contact contact = new Contact();
            contact.setFirstname(firstName);
            contact.setLastname(lastName);
            contact.setPhoneNumber(phoneNumber);
            contact.setEmail(email);
            
            // Access database to add contact
            ContactDataAccess contactData = new ContactDataAccess();
            if (contactData.addContact(contact)) {
                JOptionPane.showMessageDialog(null, "Contact added successfully");
                updateContactList();
            } else {
                JOptionPane.showMessageDialog(null, "An error occurred");
            }
        });
        
        // Update contact list button listener
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
        
        // Delete button from contact list listener
        contactView.addDeleteButtonListener(e -> {
            Contact selectedContact = getSelectedContact();
            if (selectedContact == null) {
                JOptionPane.showMessageDialog(null, "No contact selected.");
                return;
            }
            
            // Confirms they really want to delete contact
            int confirm = JOptionPane.showConfirmDialog(
                contactView,
                "Are you sure you want to delete this contact?",
                "Delete Contact",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            // Yes option to delete
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
        
        // Login button listener
        contactView.addLogoutButtonListener(e -> {
            UserDataAccess.currentUserId = 0;
            contactView.setVisible(false);
            LoginView lv = new LoginView();
            lv.setVisible(true);
            new LoginController(lv);
        });
        
        // Selection for contact list listener
        contactView.getContactTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Contact contact = getSelectedContact();
                if (contact != null) {
                    updateFields(contact);
                }
            }
        });
        
        // Search button listener
        contactView.addSearchButtonListener(e -> {
            searchView.setVisible(true);
        });
        
        // Export button listener
        contactView.addExportButtonListener(e -> {
            exportContacts();
        });
        
    }
    
    // Updates contact list after any changes
    private void updateContactList() {
        ContactDataAccess data = new ContactDataAccess();
        List<Contact> contacts = data.getContacts();
        contactView.setContactsToModel(contacts);
    }
    
    // Selects contact on the list and updates the input fields with the data
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
    
    // Updates fields with current contact on the list
    private void updateFields(Contact contact) {
        contactView.getFirstNameField().setText(contact.getFirstname());
        contactView.getLastNameField().setText(contact.getLastname());
        contactView.getPhoneNumberField().setText(contact.getPhoneNumber());
        contactView.getEmailField().setText(contact.getEmail());
    }
    
    // Exports contact list to CSV or emails list
    private void exportContacts() {
        // Create options for the user
        Object[] options = {"Save File", "Email File", "Cancel"};
        int choice = JOptionPane.showOptionDialog(contactView,
                "Do you want to save the file locally or email it?",
                "Export Contacts",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[2]);

        switch (choice) {
            case JOptionPane.YES_OPTION: // Save to computer
                saveFileLocally();
                break;
            case JOptionPane.NO_OPTION: // Email File
                emailFile();
                break;
            case JOptionPane.CANCEL_OPTION: // Cancel
                // Cancel option
                break;
            default:
                break;
        }
    }
    
    // Saves CSV file to local computer
    private void saveFileLocally() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");
        int userSelection = fileChooser.showSaveDialog(contactView);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (PrintWriter pw = new PrintWriter(fileToSave)) {
                exportDataToFile(pw);
                JOptionPane.showMessageDialog(contactView, "Data exported successfully to " + fileToSave.getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(contactView, "Error exporting data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // E-mails file
    private void emailFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select location to save the file temporarily");
        int userSelection = fileChooser.showSaveDialog(contactView);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (PrintWriter pw = new PrintWriter(fileToSave)) {
                exportDataToFile(pw);
                String recipientEmail = JOptionPane.showInputDialog(contactView, "Enter the email address to send the file:");
                if (recipientEmail != null && !recipientEmail.isEmpty()) {
                    //sendEmailWithAttachment(recipientEmail, fileToSave); // Send the email with the attachment -  not working
                }
                fileToSave.delete(); // delete the file after sending
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(contactView, "Error preparing email: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Takes contact table to be turned into a string
    private void exportDataToFile(PrintWriter pw) {
        TableModel model = contactView.getContactTable().getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < model.getColumnCount(); j++) {
                sb.append(model.getValueAt(i, j).toString());
                if (j < model.getColumnCount() - 1) sb.append(",");
            }
            pw.println(sb.toString());
        }
    }
    
    // Sends email with attachment
    private void sendEmailWithAttachment(String recipientEmail, File fileToSave) throws MessagingException {
        // I can't get this to work using Java Mail
    }
}
