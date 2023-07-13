package com.example.demo.service.booking;

import com.example.demo.model.Booking;
import com.example.demo.model.IsConfirm;
import com.example.demo.model.User;
import com.example.demo.repository.IBookingRepository;
import com.example.demo.security.userprincal.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class BookingServiceIMPL implements IBookingService {
    @Autowired
    IBookingRepository bookingRepository;
    @Autowired
    private UserDetailService userDetailService;
    @Override
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    public void save(Booking booking) {
        bookingRepository.save(booking);
    }

    @Override
    public Page<Booking> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public List<Booking> getListBookingByIsConfirm() {
        List<Booking> bookingListAll = bookingRepository.findAll();
        List<Booking> bookingList = new ArrayList<>();
        User user = userDetailService.getCurrentUser();
        for (Booking booking : bookingListAll) {
            if (booking.getIsConfirm() == IsConfirm.LOADING && booking.getUser().getId() == user.getId()) {
                bookingList.add(booking);
            }
        }
        return bookingList;
    }

    @Override
    public List<Booking> findByUserId(Long id) {
        return bookingRepository.findByUserId(id);
    }

    @Override
    public List<Booking> findByTimeSlotId(Long id) {
        return bookingRepository.findByTimeSlotId(id);
    }
}
