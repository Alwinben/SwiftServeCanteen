package com.canteen.app;

import com.canteen.repository.MySQLOrderRepository;
import com.canteen.repository.OrderRepository;
import com.canteen.service.OrderSystemLogic;
import com.canteen.ui.AdminUI;
import com.canteen.ui.UI;
import com.canteen.ui.LoginFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class CanteenOrderSystem { 
    public static void main(String[] args) {
        // Explicitly load MySQL Driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, 
                "MySQL JDBC Driver not found. Ensure the connector JAR is in your classpath.", 
                "Driver Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        SwingUtilities.invokeLater(() -> {
            try {
                // 1. Composition Root: Instantiate dependencies
                OrderRepository repository = new MySQLOrderRepository(); 
                OrderSystemLogic logic = new OrderSystemLogic(repository);

                // 2. Start the application with the Login Frame
                LoginFrame login = new LoginFrame(logic);
                login.setVisible(true);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Failed to initialize application. Check database connection and credentials.", 
                    "Initialization Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}