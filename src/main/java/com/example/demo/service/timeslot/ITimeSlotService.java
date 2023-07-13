package com.example.demo.service.timeslot;

import com.example.demo.model.TimeSlot;
import com.example.demo.service.IGenericService;

import java.util.List;

public interface ITimeSlotService extends IGenericService<TimeSlot> {
    List<TimeSlot> getTimeSlotsByDoctorId(Long id);
    boolean existsByTimesAndDoctorId(String times, Long id);


}
