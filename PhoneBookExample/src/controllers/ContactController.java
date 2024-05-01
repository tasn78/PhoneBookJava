package controllers;

import views.*;
import java.awt.event.*;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;

import models.*;
import java.util.List;
import javax.swing.event.*;

public class ContactController {

	private ContactView contactView;
	
	public ContactController(ContactView cv) {
		
		this.contactView = cv;
		
		updateContactList();
		
		contactView.addAddButtonListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// create an object of a contact
				Contact contact = new Contact();
				contact.setFirstname(contactView.getFirstName());
				contact.setLastname(contactView.getLastName());
				contact.setPhoneNumber(contactView.getPhoneNumber());
				
				ContactDataAccess contactData = new ContactDataAccess();
				if(contactData.addContact(contact)) {
					JOptionPane.showMessageDialog(null, "Contact added successfully");
					updateContactList();
				}
				else {
					JOptionPane.showMessageDialog(null, "An error occured");
				}
			}
		});
		
		contactView.addDeleteButtonListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		
		contactView.addContactListListener(new ListSelectionListener() {
		    public void valueChanged(ListSelectionEvent e) {
		        if (!e.getValueIsAdjusting()) {
		            Contact contact = getSelectedContact();
		            if (contact != null) {
		                updateFields(contact);
		            } else {
		                System.out.println("No contact found with current selection");
		            }
		        }
		    }
		});
		

		contactView.addUpdateButtonListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        Contact contact = getSelectedContact();
		        if (contact == null) {
		            JOptionPane.showMessageDialog(null, "No contact selected.");
		            return;
		        }
		        contact.setFirstname(contactView.getFirstName());
		        contact.setLastname(contactView.getLastName());
		        contact.setPhoneNumber(contactView.getPhoneNumber());

		        if (new ContactDataAccess().updateContact(contact)) {
		            JOptionPane.showMessageDialog(null, "Contact updated successfully");
		            updateContactList();  // Refresh the list
		        } else {
		            JOptionPane.showMessageDialog(null, "Update failed. Please try again.");
		        }
		    }
		});
		
		
		contactView.addDeleteButtonListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        Contact selectedContact = getSelectedContact();
		        if (selectedContact == null) {
		            JOptionPane.showMessageDialog(null, "No contact selected.");
		            return;
		        }
		        
		        // Confirm the deletion
		        int confirm = JOptionPane.showConfirmDialog(
		            contactView,
		            "Are you sure you want to delete this contact?",
		            "Delete Contact",
		            JOptionPane.YES_NO_OPTION,   //From ChatGPT
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
		    }
		});
		
		
		contactView.addLogoutButtonListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				UserDataAccess.currentUserId = 0;
				
				contactView.setVisible(false);
				
				LoginView lv = new LoginView();
				lv.setVisible(true);
				new LoginController(lv);
			}
		});
	}

	
	private void updateContactList() {
		ContactDataAccess data = new ContactDataAccess();
		
		List<Contact> contacts = data.getContacts();
			
		contactView.setContactsToModel(contacts);
	}
	
	private Contact getSelectedContact() {
	    int index = contactView.getContactList().getSelectedIndex();
	    System.out.println("Selected index: " + index);
	    if (index != -1) {
	        Contact contact = new ContactDataAccess().getContacts().get(index);
	        System.out.println("Selected contact: " + contact.getFirstname());
	        return contact;
	    }
	    return null;
	}
	
	private void updateFields(Contact contact) {
		
		contactView.getFirstNameField().setText(contact.getFirstname());
		contactView.getLastNameField().setText(contact.getLastname());
		contactView.getPhoneNumberField().setText(contact.getPhoneNumber());
		
	}
}
