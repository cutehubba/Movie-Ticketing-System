package com.movie.ticketing.userservice.controller;

import com.movie.ticketing.userservice.entity.Order;
import com.movie.ticketing.userservice.entity.Payment;
import com.movie.ticketing.userservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllOrders() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Order> orders = orderService.getAllOrders();
            response.put("code", 200);
            response.put("message", "查询所有订单成功");
            response.put("data", orders);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "查询失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOrderById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Order order = orderService.getOrderById(id);
            if (order != null) {
                response.put("code", 200);
                response.put("message", "查询订单成功");
                response.put("data", order);
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 404);
                response.put("message", "订单未找到");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "查询订单出错：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addOrder(@RequestBody Order order) {
        Map<String, Object> response = new HashMap<>();
        try {
            Order savedOrder = orderService.saveOrder(order);
            response.put("code", 200);
            response.put("message", "添加订单成功");
            response.put("data", savedOrder);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 400);
            response.put("message", "添加订单失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateOrder(@PathVariable Long id, @RequestBody Order orderDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            Order updatedOrder = orderService.updateOrder(id, orderDetails);
            if (updatedOrder != null) {
                response.put("code", 200);
                response.put("message", "更新订单成功");
                response.put("data", updatedOrder);
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 404);
                response.put("message", "要更新的订单不存在");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            response.put("code", 400);
            response.put("message", "更新失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteOrder(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            orderService.deleteOrder(id);
            response.put("code", 200);
            response.put("message", "删除订单成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "删除出错：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // 创建支付订单
    @PostMapping("/payment/{orderId}")
    public ResponseEntity<Map<String, Object>> createPayment(@PathVariable Long orderId, @RequestBody Payment payment) {
        Map<String, Object> response = new HashMap<>();
        try {
            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                response.put("code", 404);
                response.put("message", "订单未找到");
                return ResponseEntity.status(404).body(response);
            }

            // 设置支付方式
            payment.setPaymentMethod(payment.getPaymentMethod());

            // 计算支付金额
            payment.setAmount(order.getTotalPrice());

            // 设置默认支付状态为 pending
            payment.setStatus("pending");

            // 保存支付记录
            Payment savedPayment = orderService.savePayment(payment);

            response.put("code", 200);
            response.put("message", "创建支付订单成功");
            response.put("data", savedPayment);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 400);
            response.put("message", "创建支付订单失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/payment/confirm/{paymentId}")
    public ResponseEntity<Map<String, Object>> confirmPayment(@PathVariable Long paymentId, @RequestParam String status) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 更新支付状态
            Payment payment = new Payment();
            payment.setId(paymentId);
            payment.setStatus(status);
            Payment updatedPayment = orderService.updatePayment(paymentId, payment);

            response.put("code", 200);
            response.put("message", "支付状态更新成功");
            response.put("data", updatedPayment);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 400);
            response.put("message", "支付状态更新失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 处理支付回调
    @PostMapping("/payment/callback")
    public ResponseEntity<Map<String, Object>> handlePaymentCallback(@RequestBody Payment paymentCallbackData) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 更新支付记录
            Payment updatedPayment = orderService.updatePayment(paymentCallbackData.getId(), paymentCallbackData);

            response.put("code", 200);
            response.put("message", "支付回调处理成功");
            response.put("data", updatedPayment);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 400);
            response.put("message", "支付回调处理失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}