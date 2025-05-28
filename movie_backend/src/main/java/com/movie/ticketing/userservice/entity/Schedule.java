package com.movie.ticketing.userservice.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Setter
@Getter
public class Schedule {
    // Getters and Setters
    private Long id;
    private Integer movieId;
    private Integer cinemaId;
    private Long hallId;
    private Instant startTime;
    private Instant endTime;
    private BigDecimal price;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;

}