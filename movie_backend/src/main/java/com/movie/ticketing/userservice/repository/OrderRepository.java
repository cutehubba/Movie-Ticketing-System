package com.movie.ticketing.userservice.repository;

import com.movie.ticketing.userservice.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class OrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Order> getAllOrders() {
        String sql = "SELECT * FROM orders";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Order order = new Order();
            order.setId(rs.getLong("id"));
            order.setUserId(rs.getLong("user_id"));
            order.setScheduleId(rs.getLong("schedule_id"));
            order.setSeatNumbers(rs.getString("seat_numbers"));
            order.setTotalPrice(rs.getDouble("total_price"));
            order.setStatus(rs.getString("status"));
            order.setCreatedAt(rs.getString("created_at"));
            order.setUpdatedAt(rs.getString("updated_at"));
            return order;
        });
    }


    public Order getOrderById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Order order = new Order();
            order.setId(rs.getLong("id"));
            order.setUserId(rs.getLong("user_id"));
            order.setScheduleId(rs.getLong("schedule_id"));
            order.setSeatNumbers(rs.getString("seat_numbers"));
            order.setTotalPrice(rs.getDouble("total_price"));
            order.setStatus(rs.getString("status"));
            order.setCreatedAt(rs.getString("created_at"));
            order.setUpdatedAt(rs.getString("updated_at"));
            return order;
        });
    }


    public Order saveOrder(Order order) {
        String sql = "INSERT INTO orders (user_id, schedule_id, seat_numbers, total_price, status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, NOW(), NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                    ps.setLong(1, order.getUserId());
                    ps.setLong(2, order.getScheduleId());
                    ps.setString(3, order.getSeatNumbers());
                    ps.setDouble(4, order.getTotalPrice());
                    ps.setString(5, order.getStatus());
                    return ps;
                },
                keyHolder
        );
        Long id = keyHolder.getKey().longValue();
        order.setId(id);
        return order;
    }

    public Order updateOrder(Long id, Order orderDetails) {
        String sql = "UPDATE orders SET user_id = ?, schedule_id = ?, seat_numbers = ?, total_price = ?, status = ?, created_at = ?, updated_at = ? WHERE id = ?";
        jdbcTemplate.update(sql, orderDetails.getUserId(), orderDetails.getScheduleId(), orderDetails.getSeatNumbers(), orderDetails.getTotalPrice(), orderDetails.getStatus(), orderDetails.getCreatedAt(), orderDetails.getUpdatedAt(), id);
        return orderDetails;
    }

    public void deleteOrder(Long id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}