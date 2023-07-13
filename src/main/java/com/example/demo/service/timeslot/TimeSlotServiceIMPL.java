package com.example.demo.service.timeslot;

import com.example.demo.model.TimeSlot;
import com.example.demo.repository.ITimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class TimeSlotServiceIMPL implements ITimeSlotService{
    @Autowired
    private ITimeSlotRepository timeSlotRepository;

    @Override
    public List<TimeSlot> findAll() {
        return timeSlotRepository.findAll();
    }

    @Override
    public void save(TimeSlot timeSlot) {
    timeSlotRepository.save(timeSlot);
    }

    @Override
    public Page<TimeSlot> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<TimeSlot> findById(Long id) {
        return timeSlotRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        timeSlotRepository.deleteById(id);
    }

    @Override
    public List<TimeSlot> getTimeSlotsByDoctorId(Long id) {
        return timeSlotRepository.getTimeSlotsByDoctorId(id);
    }

    @Override
    public boolean existsByTimesAndDoctorId(String times, Long id) {
        return timeSlotRepository.existsByTimesAndDoctorId(times,id);
    }

}
