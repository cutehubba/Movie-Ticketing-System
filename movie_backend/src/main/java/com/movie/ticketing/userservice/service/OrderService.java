package com.movie.ticketing.userservice.service;

import com.movie.ticketing.userservice.entity.Order;
import com.movie.ticketing.userservice.entity.Payment;
import com.movie.ticketing.userservice.repository.OrderRepository;
import com.movie.ticketing.userservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SeatService seatService; // 注入座位服务

    @Autowired
    private PaymentRepository paymentRepository; // 注入支付记录仓库

    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public Order getOrderById(Long id) {
        return orderRepository.getOrderById(id);
    }

    public Order saveOrder(Order order) {
        return orderRepository.saveOrder(order);
    }

    @Transactional
    public Order updateOrder(Long id, Order orderDetails) {
        Order existingOrder = orderRepository.getOrderById(id);
        if (existingOrder == null) {
            throw new IllegalArgumentException("订单不存在");
        }

        // 获取当前订单状态和新的订单状态
        String currentStatus = existingOrder.getStatus();
        String newStatus = orderDetails.getStatus();

        // 更新订单状态
        Order updatedOrder = orderRepository.updateOrder(id, orderDetails);

        // 如果订单状态从“已支付”变为其他状态，释放座位
        if ("paid".equals(currentStatus) && !"paid".equals(newStatus)) {
            String[] seatNumbers = existingOrder.getSeatNumbers().split(",");
            for (String seatNumber : seatNumbers) {
                seatService.releaseSeat(existingOrder.getScheduleId(), seatNumber.trim());
            }
        }

        // 如果订单状态从其他状态变为“已支付”，占用座位
        if (!"paid".equals(currentStatus) && "paid".equals(newStatus)) {
            String[] seatNumbers = existingOrder.getSeatNumbers().split(",");
            for (String seatNumber : seatNumbers) {
                seatService.occupySeat(existingOrder.getScheduleId(), seatNumber.trim());
            }
        }

        return updatedOrder;
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteOrder(id);
    }

    // 保存支付记录
    public Payment savePayment(Payment payment) {
        return paymentRepository.savePayment(payment);
    }

    public Payment updatePayment(Long id, Payment paymentDetails) {
        Payment updatedPayment = paymentRepository.updatePayment(id, paymentDetails);
        if (updatedPayment != null) {
            // 根据支付状态更新订单状态
            Long orderId = updatedPayment.getOrderId();
            Order order = orderRepository.getOrderById(orderId);
            if (order != null) {
                if ("success".equals(updatedPayment.getStatus())) {
                    // 支付成功，更新订单状态为“已支付”
                    order.setStatus("paid");
                } else if ("failed".equals(updatedPayment.getStatus())) {
                    // 支付失败，更新订单状态为“未支付”
                    order.setStatus("pending");
                }
                // 更新订单状态
                orderRepository.updateOrder(orderId, order);

                // 更新座位状态
                String[] seatNumbers = order.getSeatNumbers().split(",");
                for (String seatNumber : seatNumbers) {
                    if ("success".equals(updatedPayment.getStatus())) {
                        // 支付成功，占用座位
                        seatService.occupySeat(order.getScheduleId(), seatNumber.trim());
                    } else if ("failed".equals(updatedPayment.getStatus())) {
                        // 支付失败，释放座位
                        seatService.releaseSeat(order.getScheduleId(), seatNumber.trim());
                    }
                }
            }
        }
        return updatedPayment;
    }
}