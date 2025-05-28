package com.movie.ticketing.userservice.controller;

import com.movie.ticketing.userservice.entity.Schedule;
import com.movie.ticketing.userservice.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllSchedules() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Schedule> schedules = scheduleService.getAllSchedules();
            response.put("code", 200);
            response.put("message", "查询所有排片成功");
            response.put("data", schedules);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "查询失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getScheduleById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Schedule schedule = scheduleService.getScheduleById(id);
            if (schedule != null) {
                response.put("code", 200);
                response.put("message", "查询排片成功");
                response.put("data", schedule);
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 404);
                response.put("message", "排片未找到");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "查询排片出错：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addSchedule(@RequestBody Schedule schedule) {
        Map<String, Object> response = new HashMap<>();
        try {
            Schedule savedSchedule = scheduleService.saveSchedule(schedule);
            response.put("code", 200);
            response.put("message", "添加排片成功");
            response.put("data", savedSchedule);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 400);
            response.put("message", "添加排片失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateSchedule(@PathVariable Long id, @RequestBody Schedule scheduleDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            Schedule updatedSchedule = scheduleService.updateSchedule(id, scheduleDetails);
            if (updatedSchedule != null) {
                response.put("code", 200);
                response.put("message", "更新排片成功");
                response.put("data", updatedSchedule);
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 404);
                response.put("message", "要更新的排片不存在");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            response.put("code", 400);
            response.put("message", "更新失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteSchedule(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            scheduleService.deleteSchedule(id);
            response.put("code", 200);
            response.put("message", "删除排片成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "删除出错：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}