package com.example.demo.repository;

import com.example.demo.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface IBookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long id);
    List<Booking> findByTimeSlotId(Long id);
}
