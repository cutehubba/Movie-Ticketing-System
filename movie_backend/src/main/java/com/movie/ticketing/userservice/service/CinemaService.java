package com.movie.ticketing.userservice.service;

import com.movie.ticketing.userservice.entity.Cinema;
import com.movie.ticketing.userservice.repository.CinemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CinemaService {
    @Autowired
    private CinemaRepository cinemaRepository;

    public List<Cinema> getAllCinemas() {
        return cinemaRepository.getAllCinemas();
    }

    public Cinema getCinemaById(Long id) {
        return cinemaRepository.getCinemaById(id);
    }

    public Cinema saveCinema(Cinema cinema) {
        return cinemaRepository.saveCinema(cinema);
    }

    public Cinema updateCinema(Long id, Cinema cinemaDetails) {
        return cinemaRepository.updateCinema(id, cinemaDetails);
    }

    public void deleteCinema(Long id) {
        cinemaRepository.deleteCinema(id);
    }
}