package com.example.demo.repository;

import com.example.demo.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ITimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> getTimeSlotsByDoctorId(Long id);

    boolean existsByTimesAndDoctorId(String times, Long id);
//    Date findByDate_book(Date date_book);
}
