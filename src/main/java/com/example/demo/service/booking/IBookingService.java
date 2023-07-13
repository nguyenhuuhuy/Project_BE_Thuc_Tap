package com.example.demo.service.booking;

import com.example.demo.model.Booking;
import com.example.demo.service.IGenericService;

import java.util.List;
import java.util.Optional;

public interface IBookingService extends IGenericService<Booking> {
    List<Booking> getListBookingByIsConfirm();
    List<Booking> findByUserId(Long id);
    List<Booking> findByTimeSlotId(Long id);
}