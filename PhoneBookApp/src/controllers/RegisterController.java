package controllers;

import java.awt.event.*;

import javax.swing.JOptionPane;

import models.User;
import models.UserDataAccess;
import views.*;

// Used to control the RegisterView
public class RegisterController {
	
    private RegisterView registerView;
    
    public RegisterController(RegisterView registerView) {
    	
        this.registerView = registerView;
        
        registerView.addRegisterButtonListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = registerView.getUsername().trim();
                String password = new String(registerView.getPassword().trim());
                String confirmPassword = new String(registerView.getConfirmPassword().trim());
                
                // Verifies user name is not taken or empty and follows password rules
                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Fields cannot be empty", "Registration Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Confirms same password is used in both input boxes
                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(null, "Passwords do not match", "Registration Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Follows password rules
                if (!isValidPassword(password)) {
                    return; 
                }
                
                // Confirms user name does or does not exit
                if (new UserDataAccess().doesUsernameExist(username)) {
                    JOptionPane.showMessageDialog(null, "Username already exists", "Registration Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                
                UserDataAccess uda = new UserDataAccess();
                
                // Creates new user
                if (uda.registerUser(user)) {
                    JOptionPane.showMessageDialog(null, "Registered successfully");
                    registerView.setVisible(false);
                    
                    LoginView loginView = new LoginView();
                    new LoginController(loginView);
                    loginView.setVisible(true);
                } 
                // Lets user know registration failed
                else {
                    JOptionPane.showMessageDialog(null, "Registration failed", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        registerView.addLoginButtonListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				LoginView lv = new LoginView();
				LoginController lc = new LoginController(lv);
				
				// lv.setLocationRelativeTo(null);
				lv.setVisible(true);
				
				registerView.setVisible(false);
			}
		});
    }
    
    // Set Restrictions to the user's password for strong verification
    private boolean isValidPassword(String password) {
    	// Confirms password is longer than 8 characters
        if (password.length() < 8) {
            JOptionPane.showMessageDialog(null, "Password must be at least 8 characters long", "Weak Password", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Confirms password has 1 upper case letter
        if (!password.matches(".*[A-Z].*")) {
            JOptionPane.showMessageDialog(null, "Password must contain at least one uppercase letter", "Weak Password", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Confirms password has 1 lower case letter
        if (!password.matches(".*[a-z].*")) {
            JOptionPane.showMessageDialog(null, "Password must contain at least one lowercase letter", "Weak Password", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Confirms password has one number
        if (!password.matches(".*\\d.*")) {
            JOptionPane.showMessageDialog(null, "Password must contain at least one digit", "Weak Password", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Used ChatGPT to generate symbols [1]
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            JOptionPane.showMessageDialog(null, "Password must contain at least one special character", "Weak Password", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}