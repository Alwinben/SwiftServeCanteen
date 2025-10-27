package com.canteen.ui;

import com.canteen.model.Order;
import com.canteen.service.OrderSystemLogic;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class UI extends JFrame implements OrderSystemLogic.OrderUpdateListener {
    private OrderSystemLogic orderSystemLogic;
    private AdminTokenSlip adminTokenSlip;

    private JTextField itemNameField;
    private JSpinner quantitySpinner;
    private JTextField priceField;
    private JButton placeOrderButton;
    private JButton logoutButton; // <--- NEW
    private JLabel confirmationLabel;

    public UI(OrderSystemLogic orderSystemLogic) {
        this.orderSystemLogic = orderSystemLogic;
        this.orderSystemLogic.addOrderUpdateListener(this);
        this.adminTokenSlip = new AdminTokenSlip();

        setTitle("Canteen Order System - Customer");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        initComponents();
        layoutComponents();
        addEventHandlers();
    }

    private void initComponents() {
        itemNameField = new JTextField(20);
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        priceField = new JTextField(10);
        placeOrderButton = new JButton("Place Order");
        logoutButton = new JButton("Logout"); // <--- INITIALIZED
        confirmationLabel = new JLabel("Enter your order details.");
        confirmationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        confirmationLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
    }

    private void layoutComponents() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Item Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(itemNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(quantitySpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Price ($):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(priceField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // --- MODIFIED BUTTON PANEL ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.add(placeOrderButton);
        buttonPanel.add(logoutButton); // <--- ADDED
        add(buttonPanel, BorderLayout.SOUTH);

        add(confirmationLabel, BorderLayout.NORTH);
    }

    private void addEventHandlers() {
        placeOrderButton.addActionListener(e -> placeOrder());
        
        // --- LOGOUT HANDLER ---
        logoutButton.addActionListener(e -> {
            this.dispose(); 
            LoginFrame.relaunch(orderSystemLogic);
        });
    }

    public void placeOrder() {
        String itemName = itemNameField.getText().trim();
        int quantity = (Integer) quantitySpinner.getValue();
        float price;

        // Validation (omitted for brevity)
        if (itemName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Item Name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            price = Float.parseFloat(priceField.getText().trim());
            if (price < 0) {
                JOptionPane.showMessageDialog(this, "Price cannot be negative.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid price format. Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 1. Create Order
        Order newOrder = new Order(itemName, quantity, price);

        // 2. Open Payment Window
        PaymentDialog paymentDialog = new PaymentDialog(this, newOrder);
        paymentDialog.setVisible(true);

        // 3. Check if payment was successful after the dialog closes
        if (paymentDialog.isPaymentSuccessful()) {
            // 4. Delegate to logic (DB save happens here, setting the token)
            orderSystemLogic.addOrder(newOrder); 

            // 5. UI cleanup
            itemNameField.setText("");
            quantitySpinner.setValue(1);
            priceField.setText("");

            confirmationLabel.setText("Order placed - Token #" + newOrder.getToken());

            SwingUtilities.invokeLater(() -> adminTokenSlip.displayTokenSlip(newOrder));
        } else {
            confirmationLabel.setText("Order aborted. Payment failed or cancelled.");
        }
    }

    @Override
    public void orderAdded(Order order) { /* ... */ }

    @Override
    public void orderStatusChanged(Order order) { /* ... */ }
}