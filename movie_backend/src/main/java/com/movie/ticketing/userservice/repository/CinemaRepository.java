package com.movie.ticketing.userservice.repository;

import com.movie.ticketing.userservice.entity.Cinema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CinemaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Cinema> getAllCinemas() {
        String sql = "SELECT * FROM cinemas";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Cinema cinema = new Cinema();
            cinema.setId(rs.getLong("id"));
            cinema.setName(rs.getString("name"));
            cinema.setLocation(rs.getString("location"));
            cinema.setContactNumber(rs.getString("contact_number"));
            cinema.setCapacity(rs.getInt("capacity"));
            cinema.setDescription(rs.getString("description"));
            return cinema;
        });
    }

    public Cinema getCinemaById(Long id) {
        String sql = "SELECT * FROM cinemas WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Cinema cinema = new Cinema();
            cinema.setId(rs.getLong("id"));
            cinema.setName(rs.getString("name"));
            cinema.setLocation(rs.getString("location"));
            cinema.setContactNumber(rs.getString("contact_number"));
            cinema.setCapacity(rs.getInt("capacity"));
            cinema.setDescription(rs.getString("description"));
            return cinema;
        });
    }

    public Cinema saveCinema(Cinema cinema) {
        String sql = "INSERT INTO cinemas (name, location, contact_number, capacity, description) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, cinema.getName(), cinema.getLocation(), cinema.getContactNumber(), cinema.getCapacity(), cinema.getDescription());
        return cinema;
    }

    public Cinema updateCinema(Long id, Cinema cinemaDetails) {
        String sql = "UPDATE cinemas SET name = ?, location = ?, contact_number = ?, capacity = ?, description = ? WHERE id = ?";
        jdbcTemplate.update(sql, cinemaDetails.getName(), cinemaDetails.getLocation(), cinemaDetails.getContactNumber(), cinemaDetails.getCapacity(), cinemaDetails.getDescription(), id);
        return cinemaDetails;
    }

    public void deleteCinema(Long id) {
        String sql = "DELETE FROM cinemas WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}