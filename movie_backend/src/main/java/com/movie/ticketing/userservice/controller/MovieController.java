package com.movie.ticketing.userservice.controller;

import com.movie.ticketing.userservice.entity.Movie;
import com.movie.ticketing.userservice.service.MovieService;
import com.movie.ticketing.userservice.util.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movie")
public class MovieController {

    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    private MovieService movieService;

    // 获取所有电影
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<Movie>>> getAllMovies() {
        try {
            List<Movie> movies = movieService.getAllMovies();
            ApiResponse<List<Movie>> response = new ApiResponse<>(200, "查询所有电影成功", movies);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("查询电影列表失败", e);
            ApiResponse<List<Movie>> response = new ApiResponse<>(500, "查询失败：" + e.getMessage(), null);
            return ResponseEntity.status(500).body(response);
        }
    }

    // 根据电影 ID 获取电影
    @GetMapping("/details/{id}")
    public ResponseEntity<ApiResponse<Movie>> getMovieById(@PathVariable Long id) {
        try {
            Movie movie = movieService.getMovieById(id);
            if (movie != null) {
                ApiResponse<Movie> response = new ApiResponse<>(200, "查询电影成功", movie);
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<Movie> response = new ApiResponse<>(404, "电影未找到", null);
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            logger.error("查询电影详情失败", e);
            ApiResponse<Movie> response = new ApiResponse<>(500, "查询失败：" + e.getMessage(), null);
            return ResponseEntity.status(500).body(response);
        }
    }

    // 添加电影
    @PostMapping("")
    public ResponseEntity<ApiResponse<Movie>> addMovie(@RequestBody Movie movie) {
        try {
            Movie savedMovie = movieService.saveMovie(movie);
            ApiResponse<Movie> response = new ApiResponse<>(200, "添加电影成功", savedMovie);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("添加电影失败", e);
            ApiResponse<Movie> response = new ApiResponse<>(400, "添加电影失败：" + e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 更新电影
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Movie>> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        try {
            Movie updatedMovie = movieService.updateMovie(id, movie);
            if (updatedMovie != null) {
                ApiResponse<Movie> response = new ApiResponse<>(200, "更新电影成功", updatedMovie);
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<Movie> response = new ApiResponse<>(404, "电影未找到", null);
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            logger.error("更新电影失败", e);
            ApiResponse<Movie> response = new ApiResponse<>(400, "更新电影失败：" + e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }


    // 删除电影
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMovie(@PathVariable Long id) {
        try {
            movieService.deleteMovie(id);
            ApiResponse<Void> response = new ApiResponse<>(200, "删除电影成功", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("删除电影失败", e);
            ApiResponse<Void> response = new ApiResponse<>(500, "删除失败：" + e.getMessage(), null);
            return ResponseEntity.status(500).body(response);
        }
    }
}
