package com.movie.ticketing.userservice.controller;

import com.movie.ticketing.userservice.entity.Cinema;
import com.movie.ticketing.userservice.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cinema")
public class CinemaController {
    @Autowired
    private CinemaService cinemaService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCinemas() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Cinema> cinemas = cinemaService.getAllCinemas();
            response.put("code", 200);
            response.put("message", "查询所有影院成功");
            response.put("data", cinemas);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "查询失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCinemaById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Cinema cinema = cinemaService.getCinemaById(id);
            if (cinema != null) {
                response.put("code", 200);
                response.put("message", "查询影院成功");
                response.put("data", cinema);
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 404);
                response.put("message", "影院未找到");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "查询影院出错：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addCinema(@RequestBody Cinema cinema) {
        Map<String, Object> response = new HashMap<>();
        try {
            Cinema savedCinema = cinemaService.saveCinema(cinema);
            response.put("code", 200);
            response.put("message", "添加影院成功");
            response.put("data", savedCinema);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 400);
            response.put("message", "添加影院失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCinema(@PathVariable Long id, @RequestBody Cinema cinemaDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            Cinema updatedCinema = cinemaService.updateCinema(id, cinemaDetails);
            if (updatedCinema != null) {
                response.put("code", 200);
                response.put("message", "更新影院成功");
                response.put("data", updatedCinema);
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 404);
                response.put("message", "要更新的影院不存在");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            response.put("code", 400);
            response.put("message", "更新失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCinema(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            cinemaService.deleteCinema(id);
            response.put("code", 200);
            response.put("message", "删除影院成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "删除出错：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}