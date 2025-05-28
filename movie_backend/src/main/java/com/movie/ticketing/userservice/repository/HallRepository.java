package com.movie.ticketing.userservice.repository;

import com.movie.ticketing.userservice.entity.Hall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class HallRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 获取所有影厅
    public List<Hall> getAllHalls() {
        String sql = "SELECT * FROM halls";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Hall hall = new Hall();
            hall.setId(rs.getLong("id"));
            hall.setCinemaId(rs.getInt("cinema_id"));
            hall.setName(rs.getString("name"));
            hall.setDescription(rs.getString("description"));
            hall.setSeatCount(rs.getInt("seat_count")); // 获取座位数量
            return hall;
        });
    }

    // 根据 ID 获取影厅
    public Hall getHallById(Long id) {
        String sql = "SELECT * FROM halls WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Hall hall = new Hall();
            hall.setId(rs.getLong("id"));
            hall.setCinemaId(rs.getInt("cinema_id"));
            hall.setName(rs.getString("name"));
            hall.setDescription(rs.getString("description"));
            hall.setSeatCount(rs.getInt("seat_count")); // 获取座位数量
            return hall;
        });
    }

    // 添加影厅
    public Hall saveHall(Hall hall) {
        String sql = "INSERT INTO halls (cinema_id, name, description, seat_count) VALUES (?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, hall.getCinemaId());
                ps.setString(2, hall.getName());
                ps.setString(3, hall.getDescription());
                ps.setInt(4, hall.getSeatCount());
                return ps;
            }
        }, keyHolder);
        Long id = keyHolder.getKey().longValue();
        hall.setId(id);
        return hall;
    }
    // 更新影厅
    public Hall updateHall(Long id, Hall hallDetails) {
        String sql = "UPDATE halls SET cinema_id = ?, name = ?, description = ?, seat_count = ? WHERE id = ?";
        jdbcTemplate.update(sql, hallDetails.getCinemaId(), hallDetails.getName(), hallDetails.getDescription(), hallDetails.getSeatCount(), id);
        return hallDetails;
    }

    // 删除影厅
    public void deleteHall(Long id) {
        String sql = "DELETE FROM halls WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // 根据影院 ID 获取影厅
    public List<Hall> getHallsByCinemaId(Integer cinemaId) {
        String sql = "SELECT * FROM halls WHERE cinema_id = ?";
        return jdbcTemplate.query(sql, new Object[]{cinemaId}, (rs, rowNum) -> {
            Hall hall = new Hall();
            hall.setId(rs.getLong("id"));
            hall.setCinemaId(rs.getInt("cinema_id"));
            hall.setName(rs.getString("name"));
            hall.setDescription(rs.getString("description"));
            hall.setSeatCount(rs.getInt("seat_count")); // 获取座位数量
            return hall;
        });
    }
}