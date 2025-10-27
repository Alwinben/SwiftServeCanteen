package com.canteen.repository;

import com.canteen.model.Order;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public class MySQLOrderRepository implements OrderRepository {
    // NOTE: Ensure your database name, user, and pass are correct!
    private final String JDBC_URL = "jdbc:mysql://localhost:3306/canteen_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private final String USER = "root"; 
    private final String PASS = "alwin123"; 

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASS);
    }

    @Override
    public Order save(Order order) {
        String sql = "INSERT INTO orders (item_name, quantity, price, order_time, is_complete) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, order.getItemName());
            stmt.setInt(2, order.getQuantity());
            stmt.setFloat(3, order.getPrice());
            stmt.setTimestamp(4, Timestamp.valueOf(order.getOrderTime()));
            stmt.setBoolean(5, order.isComplete());

            stmt.executeUpdate();

            // Retrieve the auto-generated token
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    order.setToken(rs.getInt(1));
                }
            }
            return order;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database error during order save.", e);
        }
    }

    @Override
    public Optional<Order> findByToken(int token) {
        String sql = "SELECT * FROM orders WHERE token = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, token);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createOrderFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Order> findAllPending() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE is_complete = FALSE ORDER BY order_time ASC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                orders.add(createOrderFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    
    @Override
    public void updateStatus(int token, boolean isComplete) {
        String sql = "UPDATE orders SET is_complete = ? WHERE token = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, isComplete);
            stmt.setInt(2, token);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Order createOrderFromResultSet(ResultSet rs) throws SQLException {
        return new Order(
            rs.getInt("token"),
            rs.getString("item_name"),
            rs.getInt("quantity"),
            rs.getFloat("price"),
            rs.getTimestamp("order_time").toLocalDateTime(),
            rs.getBoolean("is_complete")
        );
    }
}