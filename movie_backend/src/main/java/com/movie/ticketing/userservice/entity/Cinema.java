package com.movie.ticketing.userservice.entity;

import lombok.Data;

@Data
public class Cinema {
    private Long id;
    private String name;
    private String location;
    private String contactNumber;
    private Integer capacity;
    private String description;
}