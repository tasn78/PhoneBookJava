package controllers;

import java.awt.event.*;

import javax.swing.JOptionPane;

import models.User;
import models.UserDataAccess;
import views.*;

public class RegisterController {

    private RegisterView registerView;

    public RegisterController(RegisterView registerView) {
        this.registerView = registerView;

        registerView.addRegisterButtonListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = registerView.getUsername().trim();
                String password = new String(registerView.getPassword().trim());
                String confirmPassword = new String(registerView.getConfirmPassword().trim());

                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Fields cannot be empty", "Registration Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(null, "Passwords do not match", "Registration Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User user = new User();
                user.setUsername(username);
                user.setPassword(password);

                UserDataAccess uda = new UserDataAccess();

                if (uda.registerUser(user)) {
                    JOptionPane.showMessageDialog(null, "Registered successfully");
                    registerView.setVisible(false);

                    LoginView loginView = new LoginView();
                    new LoginController(loginView);
                    loginView.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Registration failed", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}