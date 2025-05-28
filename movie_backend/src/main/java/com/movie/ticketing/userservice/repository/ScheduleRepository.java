package com.movie.ticketing.userservice.repository;

import com.movie.ticketing.userservice.entity.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ScheduleRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Schedule> getAllSchedules() {
        String sql = "SELECT * FROM schedules";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Schedule schedule = new Schedule();
            schedule.setId(rs.getLong("id"));
            schedule.setMovieId(rs.getInt("movie_id"));
            schedule.setCinemaId(rs.getInt("cinema_id"));
            schedule.setHallId(rs.getLong("hall_id"));
            schedule.setStartTime(rs.getTimestamp("start_time").toInstant());
            schedule.setEndTime(rs.getTimestamp("end_time").toInstant());
            schedule.setPrice(rs.getBigDecimal("price"));
            schedule.setStatus(rs.getString("status"));
            schedule.setCreatedAt(rs.getTimestamp("created_at").toInstant());
            schedule.setUpdatedAt(rs.getTimestamp("updated_at").toInstant());
            return schedule;
        });
    }

    public Schedule getScheduleById(Long id) {
        String sql = "SELECT * FROM schedules WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Schedule schedule = new Schedule();
            schedule.setId(rs.getLong("id"));
            schedule.setMovieId(rs.getInt("movie_id"));
            schedule.setCinemaId(rs.getInt("cinema_id"));
            schedule.setHallId(rs.getLong("hall_id"));
            schedule.setStartTime(rs.getTimestamp("start_time").toInstant());
            schedule.setEndTime(rs.getTimestamp("end_time").toInstant());
            schedule.setPrice(rs.getBigDecimal("price"));
            schedule.setStatus(rs.getString("status"));
            schedule.setCreatedAt(rs.getTimestamp("created_at").toInstant());
            schedule.setUpdatedAt(rs.getTimestamp("updated_at").toInstant());
            return schedule;
        });
    }

    public Schedule saveSchedule(Schedule schedule) {
        String sql = "INSERT INTO schedules (movie_id, cinema_id, hall_id, start_time, end_time, price, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, schedule.getMovieId(), schedule.getCinemaId(), schedule.getHallId(), schedule.getStartTime(), schedule.getEndTime(), schedule.getPrice(), schedule.getStatus());
        return schedule;
    }

    public Schedule updateSchedule(Long id, Schedule scheduleDetails) {
        String sql = "UPDATE schedules SET movie_id = ?, cinema_id = ?, hall_id = ?, start_time = ?, end_time = ?, price = ?, status = ? WHERE id = ?";
        jdbcTemplate.update(sql, scheduleDetails.getMovieId(), scheduleDetails.getCinemaId(), scheduleDetails.getHallId(), scheduleDetails.getStartTime(), scheduleDetails.getEndTime(), scheduleDetails.getPrice(), scheduleDetails.getStatus(), id);
        return scheduleDetails;
    }

    public void deleteSchedule(Long id) {
        String sql = "DELETE FROM schedules WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}