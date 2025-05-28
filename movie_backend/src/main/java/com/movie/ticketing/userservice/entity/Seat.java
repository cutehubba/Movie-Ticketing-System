package com.movie.ticketing.userservice.entity;

import lombok.Getter;
import lombok.Setter;

public class Seat {
    // Getters and Setters
    @Setter
    @Getter
    private Long id;
    @Setter
    @Getter
    private Long hallId;
    @Setter
    @Getter
    private String seatNumber;
    private boolean isOccupied;


    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
}