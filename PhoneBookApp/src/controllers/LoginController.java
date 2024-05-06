package controllers;

import java.awt.event.*;

import javax.swing.JOptionPane;

import views.*;
import models.*;

// Controls LoginView
public class LoginController {

    LoginView loginView;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        
        // Adds register button
        loginView.addRegisterButtonListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Hide the current login view
                loginView.setVisible(false);

                // Create and display the register view
                RegisterView registerView = new RegisterView();
                RegisterController registerController = new RegisterController(registerView);
                registerView.setVisible(true);
            }
        });
        
        // Login button listener
        loginView.addLoginButtonListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserDataAccess userData = new UserDataAccess();
                if (userData.loginUser(loginView.getUsername(), loginView.getPassword())) {
                    // If login is successful, hide the login view and show the contact view
                    loginView.setVisible(false);
                    
                    ContactView contactView = new ContactView();
                    new ContactController(contactView);
                    contactView.setVisible(true);
                } else {
                    // Display an error message if login fails
                    JOptionPane.showMessageDialog(null, "Username or password is incorrect");
                }
            }
        });
    }
}
