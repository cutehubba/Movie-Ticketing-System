package com.movie.ticketing.userservice.repository;

import com.movie.ticketing.userservice.entity.Seat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SeatRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 保存座位
    public Seat saveSeat(Seat seat) {
        String sql = "INSERT INTO seats (hall_id, seat_number, is_occupied) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, seat.getHallId(), seat.getSeatNumber(), seat.isOccupied());
        return seat;
    }

    // 获取影厅的所有座位
    public List<Seat> getSeatsByHallId(Long hallId) {
        String sql = "SELECT * FROM seats WHERE hall_id = ?";
        return jdbcTemplate.query(sql, new Object[]{hallId}, (rs, rowNum) -> {
            Seat seat = new Seat();
            seat.setId(rs.getLong("id"));
            seat.setHallId(rs.getLong("hall_id"));
            seat.setSeatNumber(rs.getString("seat_number"));
            seat.setOccupied(rs.getBoolean("is_occupied"));
            return seat;
        });
    }

    // 标记座位为占用
    public void occupySeat(Long seatId) {
        String sql = "UPDATE seats SET is_occupied = TRUE WHERE id = ?";
        jdbcTemplate.update(sql, seatId);
    }

    // 标记座位为未占用
    public void releaseSeat(Long seatId) {
        String sql = "UPDATE seats SET is_occupied = FALSE WHERE id = ?";
        jdbcTemplate.update(sql, seatId);
    }

    // 根据schedule_id和seat_number获取座位
    public Seat getSeatByScheduleIdAndSeatNumber(Long scheduleId, String seatNumber) {
        String sql = "SELECT s.* FROM seats s " +
                "JOIN schedules sch ON s.hall_id = sch.hall_id " +
                "WHERE sch.id = ? AND s.seat_number = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{scheduleId, seatNumber}, (rs, rowNum) -> {
            Seat seat = new Seat();
            seat.setId(rs.getLong("id"));
            seat.setHallId(rs.getLong("hall_id"));
            seat.setSeatNumber(rs.getString("seat_number"));
            seat.setOccupied(rs.getBoolean("is_occupied"));
            return seat;
        });
    }

    // 根据schedule_id和seat_number更新座位状态
    public void updateSeatStatusByScheduleIdAndSeatNumber(Long scheduleId, String seatNumber, boolean isOccupied) {
        String sql = "UPDATE seats s " +
                "JOIN schedules sch ON s.hall_id = sch.hall_id " +
                "SET s.is_occupied = ? " +
                "WHERE sch.id = ? AND s.seat_number = ?";
        jdbcTemplate.update(sql, isOccupied, scheduleId, seatNumber);
    }
}