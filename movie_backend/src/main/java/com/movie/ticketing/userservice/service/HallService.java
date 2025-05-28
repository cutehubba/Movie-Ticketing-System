package com.movie.ticketing.userservice.service;

import com.movie.ticketing.userservice.entity.Hall;
import com.movie.ticketing.userservice.entity.Seat;
import com.movie.ticketing.userservice.repository.HallRepository;
import com.movie.ticketing.userservice.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HallService {
    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private SeatRepository seatRepository;

    public List<Hall> getAllHalls() {
        return hallRepository.getAllHalls();
    }

    public Hall getHallById(Long id) {
        return hallRepository.getHallById(id);
    }

    public Hall saveHall(Hall hall) {
        Hall savedHall = hallRepository.saveHall(hall);
        if (savedHall != null) {
            // 确保 savedHall.getId() 不为 null
            Long hallId = savedHall.getId();
            System.out.println("Initializing seats for hall ID: " + hallId);
            if (hallId != null) {
                // 初始化影厅座位
                initializeSeats(hallId, hall.getSeatCount());
            }
        }
        return savedHall;
    }

    public Hall updateHall(Long id, Hall hallDetails) {
        return hallRepository.updateHall(id, hallDetails);
    }

    public void deleteHall(Long id) {
        hallRepository.deleteHall(id);
    }

    // 初始化影厅座位
    public void initializeSeats(Long hallId, int seatCount) {
        System.out.println("Initializing seats for hall ID: " + hallId);
        for (int i = 1; i <= seatCount; i++) {
            String seatNumber = "座位" + i;
            Seat seat = new Seat();
            seat.setHallId(hallId);
            seat.setSeatNumber(seatNumber);
            seat.setOccupied(false);
            seatRepository.saveSeat(seat);
        }
    }

    // 获取影厅的所有座位
    public List<Seat> getSeatsByHallId(Long hallId) {
        return seatRepository.getSeatsByHallId(hallId);
    }

    // 标记座位为占用
    public void occupySeats(Long hallId, List<String> seatNumbers) {
        List<Seat> seats = seatRepository.getSeatsByHallId(hallId);
        for (Seat seat : seats) {
            if (seatNumbers.contains(seat.getSeatNumber())) {
                seatRepository.occupySeat(seat.getId());
            }
        }
    }

    // 标记座位为未占用
    public void releaseSeats(Long hallId, List<String> seatNumbers) {
        List<Seat> seats = seatRepository.getSeatsByHallId(hallId);
        for (Seat seat : seats) {
            if (seatNumbers.contains(seat.getSeatNumber())) {
                seatRepository.releaseSeat(seat.getId());
            }
        }
    }

    // 根据影院 ID 获取影厅
    public List<Hall> getHallsByCinemaId(Integer cinemaId) {
        return hallRepository.getHallsByCinemaId(cinemaId);
    }
}