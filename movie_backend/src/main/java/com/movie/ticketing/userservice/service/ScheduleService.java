package com.movie.ticketing.userservice.service;

import com.movie.ticketing.userservice.entity.Schedule;
import com.movie.ticketing.userservice.entity.Seat;
import com.movie.ticketing.userservice.repository.HallRepository;
import com.movie.ticketing.userservice.repository.ScheduleRepository;
import com.movie.ticketing.userservice.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private SeatRepository seatRepository;

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.getAllSchedules();
    }

    public Schedule getScheduleById(Long id) {
        return scheduleRepository.getScheduleById(id);
    }

    public Schedule saveSchedule(Schedule schedule) {
        if (hallRepository.getHallById(schedule.getHallId()) == null) {
            throw new IllegalArgumentException("影厅不存在");
        }
        return scheduleRepository.saveSchedule(schedule);
    }

    public Schedule updateSchedule(Long id, Schedule scheduleDetails) {
        if (hallRepository.getHallById(scheduleDetails.getHallId()) == null) {
            throw new IllegalArgumentException("影厅不存在");
        }
        return scheduleRepository.updateSchedule(id, scheduleDetails);
    }

    public void deleteSchedule(Long id) {
        scheduleRepository.deleteSchedule(id);
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
}