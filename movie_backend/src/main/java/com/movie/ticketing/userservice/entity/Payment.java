package com.movie.ticketing.userservice.entity;

import lombok.Data;

@Data
public class Payment {
    private Long id;
    private Long orderId;
    private String paymentMethod;
    private Double amount;
    private String status;
    private String createdAt;
    private String updatedAt;
}