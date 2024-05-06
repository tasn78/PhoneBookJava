package models;

import java.sql.*;
import myutils.Util;
import java.util.*;

public class ContactDataAccess {

	private Connection connect;

	public ContactDataAccess() {
		try {
			this.connect = Util.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Adds contact to contact list in database
	public boolean addContact(Contact contact) {

		try {
			String query = "INSERT INTO tb_contacts (firstname, lastname, phonenumber, email, user) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement stm = connect.prepareStatement(query);

			stm.setString(1, contact.getFirstname());
			stm.setString(2, contact.getLastname());
			stm.setString(3, contact.getPhoneNumber());
			stm.setString(4, contact.getEmail());
			stm.setInt(5, UserDataAccess.currentUserId);

			int row = stm.executeUpdate();

			if (row > 0)
				return true;
			else
				return false;

		} catch (SQLException e) {
			return false;
		}
	}
	
	// Updates the contact in the database
	public boolean updateContact(Contact contact) {

		try {
			String query = "UPDATE tb_contacts SET firstname=?, lastname=?, phonenumber=?, email=? WHERE id=? AND user=?";
			PreparedStatement stm = connect.prepareStatement(query);

			stm.setString(1, contact.getFirstname());
			stm.setString(2, contact.getLastname());
			stm.setString(3, contact.getPhoneNumber());
			stm.setString(4, contact.getEmail());
			stm.setInt(5, contact.getId());
			stm.setInt(6, UserDataAccess.currentUserId);

			int row = stm.executeUpdate();

			if (row > 0)
				return true;
			else
				return false;

		} catch (SQLException e) {
			return false;
		}
	}
	
	// Deletes contact from the database
	public boolean deleteContact(int id) {
	    try {
	        String query = "DELETE FROM tb_contacts WHERE id = ? AND user = ?";
	        PreparedStatement stm = connect.prepareStatement(query);

	        stm.setInt(1, id);
	        stm.setInt(2, UserDataAccess.currentUserId);  // Assuming contacts are scoped to users

	        int row = stm.executeUpdate();

	        if (row > 0)
	            return true;
	        else
	            return false;

	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	        return false;
	    }
	}
	
	// Gets the contact from the database and adds to a list
	public List<Contact> getContacts() {
		
		List<Contact> contactList = new ArrayList<>();
		
		try {
			String query = "SELECT * FROM tb_contacts WHERE user = ?";
			PreparedStatement stm = connect.prepareStatement(query);
			
			stm.setInt(1, UserDataAccess.currentUserId);
			
			ResultSet result = stm.executeQuery();
			
			while (result.next()) {
				int contactId = result.getInt("id");
				String firstName = result.getString("firstname");
				String lastName = result.getString("lastname");
				String phoneNumber = result.getString("phonenumber");
				String email = result.getString("email");
				
				Contact contact = new Contact(contactId, firstName, lastName, phoneNumber, email);
				
				contactList.add(contact);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return contactList;
	}
	
}
