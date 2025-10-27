package com.canteen.ui;

import com.canteen.model.Order;
import com.canteen.service.OrderSystemLogic;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class AdminUI extends JFrame implements OrderSystemLogic.OrderUpdateListener {
    private OrderSystemLogic orderSystemLogic;

    private JTable orderTable;
    private DefaultTableModel tableModel;
    private JButton markCompleteButton;
    private JButton refreshButton;
    private JButton logoutButton; // <--- NEW

    public AdminUI(OrderSystemLogic orderSystemLogic) {
        this.orderSystemLogic = orderSystemLogic;
        this.orderSystemLogic.addOrderUpdateListener(this);

        setTitle("Canteen Order System - Admin");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        initComponents();
        layoutComponents();
        addEventHandlers();
        refreshOrderTable();
    }

    private void initComponents() {
        String[] columnNames = {"Token", "Item", "Quantity", "Price", "Total", "Status", "Time"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(tableModel);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        markCompleteButton = new JButton("Mark as Complete");
        refreshButton = new JButton("Refresh Orders");
        logoutButton = new JButton("Logout"); // <--- INITIALIZED
    }

    private void layoutComponents() {
        JScrollPane scrollPane = new JScrollPane(orderTable);
        add(scrollPane, BorderLayout.CENTER);

        // --- MODIFIED BUTTON PANEL ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        buttonPanel.add(logoutButton); // <--- ADDED
        buttonPanel.add(refreshButton);
        buttonPanel.add(markCompleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        JLabel headerLabel = new JLabel("Pending Canteen Orders", SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(headerLabel, BorderLayout.NORTH);
    }

    private void addEventHandlers() {
        markCompleteButton.addActionListener(e -> {
            int selectedRow = orderTable.getSelectedRow();
            if (selectedRow != -1) {
                int token = (int) tableModel.getValueAt(selectedRow, 0);
                orderSystemLogic.markOrderAsComplete(token);
            } else {
                JOptionPane.showMessageDialog(AdminUI.this, "Please select an order to mark complete.", "No Order Selected", JOptionPane.WARNING_MESSAGE);
            }
        });

        refreshButton.addActionListener(e -> refreshOrderTable());
        
        // --- LOGOUT HANDLER ---
        logoutButton.addActionListener(e -> {
            this.dispose();
            LoginFrame.relaunch(orderSystemLogic);
        });
    }

    private void refreshOrderTable() {
        // ... (body remains the same)
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            List<Order> pendingOrders = orderSystemLogic.getPendingOrders();
            for (Order order : pendingOrders) {
                Vector<Object> rowData = new Vector<>();
                rowData.add(order.getToken());
                rowData.add(order.getItemName());
                rowData.add(order.getQuantity());
                rowData.add(String.format("%.2f", order.getPrice()));
                rowData.add(String.format("%.2f", order.getTotalPrice()));
                rowData.add(order.isComplete() ? "Completed" : "Pending");
                rowData.add(order.getFormattedOrderTime());
                tableModel.addRow(rowData);
            }
        });
    }

    @Override
    public void orderAdded(Order order) {
        // ... (body remains the same)
        if (!order.isComplete()) {
            SwingUtilities.invokeLater(() -> {
                Vector<Object> rowData = new Vector<>();
                rowData.add(order.getToken());
                rowData.add(order.getItemName());
                rowData.add(order.getQuantity());
                rowData.add(String.format("%.2f", order.getPrice()));
                rowData.add(String.format("%.2f", order.getTotalPrice()));
                rowData.add("Pending");
                rowData.add(order.getFormattedOrderTime());
                tableModel.addRow(rowData);
            });
        }
    }

    @Override
    public void orderStatusChanged(Order order) {
        refreshOrderTable();
    }
}