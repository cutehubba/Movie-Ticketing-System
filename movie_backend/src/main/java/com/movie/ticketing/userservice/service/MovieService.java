package com.movie.ticketing.userservice.service;

import com.movie.ticketing.userservice.entity.Movie;
import com.movie.ticketing.userservice.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    // 获取所有电影
    public List<Movie> getAllMovies() {
        return movieRepository.getAllMovies();
    }

    // 根据电影 ID 获取电影
    public Movie getMovieById(Long id) {
        return movieRepository.getMovieById(id);
    }

    // 添加电影
    public Movie saveMovie(Movie movie) {
        return movieRepository.saveMovie(movie);
    }

    // 更新电影
    public Movie updateMovie(Long id, Movie movie) {
        return movieRepository.updateMovie(id, movie);
    }

    // 删除电影
    public void deleteMovie(Long id) {
        movieRepository.deleteMovie(id);
    }
}
