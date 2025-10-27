package com.canteen.ui;

import com.canteen.model.Order;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
// *** ADD THIS IMPORT ***
import java.awt.event.ActionListener; 

// ... rest of the code
public class PaymentDialog extends JDialog {
    private Order order;
    private JTextField transactionIdField;
    private JButton submitButton;
    private boolean paymentSuccessful = false;

    public PaymentDialog(JFrame parent, Order order) {
        super(parent, "Secure Payment Gateway", true); // Modal dialog
        this.order = order;
        
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        initComponents();
        layoutComponents();
        addEventHandlers();
        
        // Display total price immediately
        ((JLabel) ((JPanel) getContentPane().getComponent(0)).getComponent(1)).setText(String.format("$%.2f", order.getTotalPrice()));
    }

    private void initComponents() {
        transactionIdField = new JTextField(15);
        submitButton = new JButton("Submit Payment");
    }

    private void layoutComponents() {
        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        
        infoPanel.add(new JLabel("Order Total:"));
        JLabel totalLabel = new JLabel();
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        infoPanel.add(totalLabel);
        
        infoPanel.add(new JLabel("Transaction ID (e.g., wallet, card):"));
        infoPanel.add(transactionIdField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);

        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addEventHandlers() {
        ActionListener processAction = e -> processPayment();
        submitButton.addActionListener(processAction);
        transactionIdField.addActionListener(processAction);
    }

    private void processPayment() {
        String transactionId = transactionIdField.getText().trim();

        if (transactionId.isEmpty() || transactionId.length() < 5) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Transaction ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- Payment successful simulation ---
        paymentSuccessful = true;
        
        // Generate a receipt pop-up
        generateReceipt(transactionId);
        
        // Close the dialog, allowing the main UI flow to continue
        dispose();
    }
    
    private void generateReceipt(String transactionId) {
        JDialog receipt = new JDialog(this, "Order Receipt - Token # Pending...", false);
        receipt.setSize(350, 270);
        receipt.setLocationRelativeTo(this);
        
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setEditable(false);
        
        // NOTE: Order token is retrieved AFTER saving to DB, so we use a placeholder here.
        // For a real system, the receipt would be generated AFTER the DB call completes.
        String receiptText = String.format(
            "=================================\n" +
            "      SWIFTSERVE RECEIPT         \n" +
            "=================================\n" +
            "TOKEN NO:     [Assigned on Save]\n" +
            "ITEM:         %s\n" +
            "QTY:          %d\n" +
            "UNIT PRICE:   $%.2f\n" +
            "---------------------------------\n" +
            "TOTAL PAID:   $%.2f\n" +
            "TRANS. ID:    %s\n" +
            "TIME:         %s\n" +
            "=================================\n" +
            "    THANK YOU FOR YOUR ORDER!    \n",
            order.getItemName(),
            order.getQuantity(),
            order.getPrice(),
            order.getTotalPrice(),
            transactionId,
            LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"))
        );
        textArea.setText(receiptText);
        
        receipt.add(new JScrollPane(textArea), BorderLayout.CENTER);
        receipt.setVisible(true);
    }

    public boolean isPaymentSuccessful() {
        return paymentSuccessful;
    }
}