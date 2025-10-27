package com.canteen.ui;

import com.canteen.model.Order;
import javax.swing.*;
import java.awt.*;

class AdminTokenSlip extends JFrame {
    private JPanel detailsPanel;

    public AdminTokenSlip() {
        setTitle("Order Token Slip");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(detailsPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void displayTokenSlip(Order order) {
        detailsPanel.removeAll();

        JLabel titleLabel = new JLabel("--- Canteen Order Slip ---");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsPanel.add(titleLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        addDetailRow("Token Number:", order.getToken() != null ? String.valueOf(order.getToken()) : "N/A");
        addDetailRow("Item Name:", order.getItemName());
        addDetailRow("Quantity:", String.valueOf(order.getQuantity()));
        addDetailRow("Price per Item:", String.format("$%.2f", order.getPrice()));
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel totalLabel = new JLabel(String.format("Total: $%.2f", order.getTotalPrice()));
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsPanel.add(totalLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        addDetailRow("Order Time:", order.getFormattedOrderTime());
        addDetailRow("Status:", order.isComplete() ? "Completed" : "Pending");
        detailsPanel.add(Box.createVerticalGlue());

        detailsPanel.revalidate();
        detailsPanel.repaint();
        setVisible(true);
    }

    private void addDetailRow(String labelText, String valueText) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        JLabel value = new JLabel(valueText);
        value.setFont(new Font("SansSerif", Font.PLAIN, 12));
        rowPanel.add(label);
        rowPanel.add(value);
        detailsPanel.add(rowPanel);
    }
}