package com.movie.ticketing.userservice.controller;

import com.movie.ticketing.userservice.entity.Hall;
import com.movie.ticketing.userservice.service.HallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hall")
public class HallController {
    @Autowired
    private HallService hallService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllHalls() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Hall> halls = hallService.getAllHalls();
            response.put("code", 200);
            response.put("message", "查询所有影厅成功");
            response.put("data", halls);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "查询失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getHallById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Hall hall = hallService.getHallById(id);
            if (hall != null) {
                response.put("code", 200);
                response.put("message", "查询影厅成功");
                response.put("data", hall);
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 404);
                response.put("message", "影厅未找到");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "查询影厅出错：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addHall(@RequestBody Hall hall) {
        Map<String, Object> response = new HashMap<>();
        try {
            Hall savedHall = hallService.saveHall(hall);
            response.put("code", 200);
            response.put("message", "添加影厅成功");
            response.put("data", savedHall);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 400);
            response.put("message", "添加影厅失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateHall(@PathVariable Long id, @RequestBody Hall hallDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            Hall updatedHall = hallService.updateHall(id, hallDetails);
            if (updatedHall != null) {
                response.put("code", 200);
                response.put("message", "更新影厅成功");
                response.put("data", updatedHall);
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 404);
                response.put("message", "要更新的影厅不存在");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            response.put("code", 400);
            response.put("message", "更新失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteHall(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            hallService.deleteHall(id);
            response.put("code", 200);
            response.put("message", "删除影厅成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "删除出错：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    // 根据影院 ID 查询影厅
    @GetMapping("/cinema/{cinemaId}")
    public ResponseEntity<Map<String, Object>> getHallsByCinemaId(@PathVariable Integer cinemaId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Hall> halls = hallService.getHallsByCinemaId(cinemaId);
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", halls);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "查询失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}