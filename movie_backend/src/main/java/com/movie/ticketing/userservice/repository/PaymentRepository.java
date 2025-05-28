package com.movie.ticketing.userservice.repository;

import com.movie.ticketing.userservice.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Payment> getAllPayments() {
        String sql = "SELECT * FROM payments";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Payment payment = new Payment();
            payment.setId(rs.getLong("id"));
            payment.setOrderId(rs.getLong("order_id"));
            payment.setPaymentMethod(rs.getString("payment_method"));
            payment.setAmount(rs.getDouble("amount"));
            payment.setStatus(rs.getString("status"));
            payment.setCreatedAt(rs.getString("created_at"));
            payment.setUpdatedAt(rs.getString("updated_at"));
            return payment;
        });
    }

    public Payment getPaymentById(Long id) {
        String sql = "SELECT * FROM payments WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Payment payment = new Payment();
            payment.setId(rs.getLong("id"));
            payment.setOrderId(rs.getLong("order_id"));
            payment.setPaymentMethod(rs.getString("payment_method"));
            payment.setAmount(rs.getDouble("amount"));
            payment.setStatus(rs.getString("status"));
            payment.setCreatedAt(rs.getString("created_at"));
            payment.setUpdatedAt(rs.getString("updated_at"));
            return payment;
        });
    }

    public Payment savePayment(Payment payment) {
        String sql = "INSERT INTO payments (order_id, payment_method, amount, status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, payment.getOrderId(), payment.getPaymentMethod(), payment.getAmount(), payment.getStatus(), payment.getCreatedAt(), payment.getUpdatedAt());
        return payment;
    }

    public Payment updatePayment(Long id, Payment paymentDetails) {
        String sql = "UPDATE payments SET order_id = ?, payment_method = ?, amount = ?, status = ?, created_at = ?, updated_at = ? WHERE id = ?";
        jdbcTemplate.update(sql, paymentDetails.getOrderId(), paymentDetails.getPaymentMethod(), paymentDetails.getAmount(), paymentDetails.getStatus(), paymentDetails.getCreatedAt(), paymentDetails.getUpdatedAt(), id);
        return paymentDetails;
    }

    public void deletePayment(Long id) {
        String sql = "DELETE FROM payments WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}