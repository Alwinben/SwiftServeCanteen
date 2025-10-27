package com.canteen.ui;

import com.canteen.service.AuthService;
import com.canteen.service.OrderSystemLogic;
import javax.swing.*;
import java.awt.*;
// *** ADD THIS IMPORT ***
import java.awt.event.ActionListener; 

// ... rest of the code

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private AuthService authService;
    private OrderSystemLogic orderSystemLogic;

    public LoginFrame(OrderSystemLogic logic) {
        this.authService = new AuthService();
        this.orderSystemLogic = logic;
        
        setTitle("SwiftServe Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initComponents();
        layoutComponents();
        addEventHandlers();
    }

    private void initComponents() {
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");
    }

    private void layoutComponents() {
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        formPanel.add(new JLabel("Username (student/admin):"));
        formPanel.add(usernameField);
        
        formPanel.add(new JLabel("Password (pass/canteen123):"));
        formPanel.add(passwordField);
        
        formPanel.add(new JLabel()); // Placeholder
        formPanel.add(loginButton);

        add(formPanel, BorderLayout.CENTER);
    }

    private void addEventHandlers() {
        ActionListener loginAction = e -> attemptLogin();
        loginButton.addActionListener(loginAction);
        passwordField.addActionListener(loginAction);
    }

    private void attemptLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        AuthService.UserRole role = authService.authenticate(username, password);

        if (role == AuthService.UserRole.INVALID) {
            JOptionPane.showMessageDialog(this, "Invalid Username or Password.", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Close login window
        this.dispose(); 

        // Launch the appropriate main application window
        if (role == AuthService.UserRole.STUDENT) {
            UI customerUI = new UI(orderSystemLogic);
            customerUI.setVisible(true);
        } else if (role == AuthService.UserRole.CANTEEN_ADMIN) {
            AdminUI adminUI = new AdminUI(orderSystemLogic);
            adminUI.setVisible(true);
        }
    }
    
    // Static method for the Logout button to call
    public static void relaunch(OrderSystemLogic logic) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame newLogin = new LoginFrame(logic);
            newLogin.setVisible(true);
        });
    }
}