package com.movie.ticketing.userservice.controller;

import com.movie.ticketing.userservice.entity.Seat;
import com.movie.ticketing.userservice.service.HallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seat")
public class SeatController {
    @Autowired
    private HallService hallService;

    @GetMapping("/hall/{hallId}")
    public ResponseEntity<Map<String, Object>> getSeatsByHallId(@PathVariable Long hallId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Seat> seats = hallService.getSeatsByHallId(hallId);
            response.put("code", 200);
            response.put("message", "查询座位成功");
            response.put("data", seats);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "查询座位出错：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/occupy")
    public ResponseEntity<Map<String, Object>> occupySeats(@RequestParam Long hallId, @RequestParam List<String> seatNumbers) {
        Map<String, Object> response = new HashMap<>();
        try {
            hallService.occupySeats(hallId, seatNumbers);
            response.put("code", 200);
            response.put("message", "座位占用成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "座位占用失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/release")
    public ResponseEntity<Map<String, Object>> releaseSeats(@RequestParam Long hallId, @RequestParam List<String> seatNumbers) {
        Map<String, Object> response = new HashMap<>();
        try {
            hallService.releaseSeats(hallId, seatNumbers);
            response.put("code", 200);
            response.put("message", "座位释放成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "座位释放失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}