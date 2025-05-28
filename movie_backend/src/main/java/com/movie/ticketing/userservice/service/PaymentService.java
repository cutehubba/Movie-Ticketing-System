package com.movie.ticketing.userservice.service;

import com.movie.ticketing.userservice.entity.Payment;
import com.movie.ticketing.userservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public List<Payment> getAllPayments() {
        return paymentRepository.getAllPayments();
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.getPaymentById(id);
    }

    public Payment savePayment(Payment payment) {
        return paymentRepository.savePayment(payment);
    }

    public Payment updatePayment(Long id, Payment paymentDetails) {
        return paymentRepository.updatePayment(id, paymentDetails);
    }

    public void deletePayment(Long id) {
        paymentRepository.deletePayment(id);
    }
}