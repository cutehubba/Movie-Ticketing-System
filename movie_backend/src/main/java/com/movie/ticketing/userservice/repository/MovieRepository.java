package com.movie.ticketing.userservice.repository;

import com.movie.ticketing.userservice.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MovieRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 获取所有电影
    public List<Movie> getAllMovies() {
        String sql = "SELECT * FROM movies";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Movie movie = new Movie();
            movie.setId(rs.getLong("id"));
            movie.setTitle(rs.getString("title"));
            movie.setReleaseDate(rs.getDate("release_date").toLocalDate());
            movie.setDuration(rs.getInt("duration"));
            movie.setGenre(rs.getString("genre"));
            movie.setDirector(rs.getString("director"));
            movie.setDescription(rs.getString("description"));
            movie.setReleaseStatus(rs.getString("release_status"));
            movie.setRating(rs.getDouble("rating"));
            movie.setPosterUrl(rs.getString("poster_url"));
            movie.setTrailerUrl(rs.getString("trailer_url"));
            return movie;
        });
    }

    // 根据电影 ID 获取电影
    public Movie getMovieById(Long id) {
        String sql = "SELECT * FROM movies WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                Movie movie = new Movie();
                movie.setId(rs.getLong("id"));
                movie.setTitle(rs.getString("title"));
                movie.setReleaseDate(rs.getDate("release_date").toLocalDate());
                movie.setDuration(rs.getInt("duration"));
                movie.setGenre(rs.getString("genre"));
                movie.setDirector(rs.getString("director"));
                movie.setDescription(rs.getString("description"));
                movie.setReleaseStatus(rs.getString("release_status"));
                movie.setRating(rs.getDouble("rating"));
                movie.setPosterUrl(rs.getString("poster_url"));
                movie.setTrailerUrl(rs.getString("trailer_url"));
                return movie;
            });
        } catch (EmptyResultDataAccessException e) {
            // 如果没有找到电影，返回 null
            return null;
        }
    }

    // 添加电影
    public Movie saveMovie(Movie movie) {
        String sql = "INSERT INTO movies (title, release_date, duration, genre, director, description, release_status, rating, poster_url, trailer_url) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, movie.getTitle(), movie.getReleaseDate(), movie.getDuration(),
                movie.getGenre(), movie.getDirector(), movie.getDescription(), movie.getReleaseStatus(),
                movie.getRating(), movie.getPosterUrl(), movie.getTrailerUrl());
        return movie;
    }

    // 更新电影
    public Movie updateMovie(Long id, Movie movie) {
        String sql = "UPDATE movies SET title = ?, release_date = ?, duration = ?, genre = ?, director = ?, description = ?, release_status = ?, rating = ?, poster_url = ?, trailer_url = ? WHERE id = ?";
        jdbcTemplate.update(sql, movie.getTitle(), movie.getReleaseDate(), movie.getDuration(),
                movie.getGenre(), movie.getDirector(), movie.getDescription(), movie.getReleaseStatus(),
                movie.getRating(), movie.getPosterUrl(), movie.getTrailerUrl(), id);
        return movie;  // 返回更新后的电影对象
    }

    // 删除电影
    public void deleteMovie(Long id) {
        String sql = "DELETE FROM movies WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
