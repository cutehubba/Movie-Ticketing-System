package com.movie.ticketing.userservice.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Order {
    @Setter
    @Getter
    private Long id;
    private Long userId;
    private Long scheduleId;
    private String seatNumbers;
    private Double totalPrice;
    private String status;
    private String createdAt;
    private String updatedAt;

}