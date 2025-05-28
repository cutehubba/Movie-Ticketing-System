package com.movie.ticketing.userservice.service;

import com.movie.ticketing.userservice.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeatService {
    @Autowired
    private SeatRepository seatRepository;

    // 标记座位为占用
    public void occupySeat(Long scheduleId, String seatNumber) {
        seatRepository.updateSeatStatusByScheduleIdAndSeatNumber(scheduleId, seatNumber, true);
    }

    // 标记座位为未占用
    public void releaseSeat(Long scheduleId, String seatNumber) {
        seatRepository.updateSeatStatusByScheduleIdAndSeatNumber(scheduleId, seatNumber, false);
    }
}