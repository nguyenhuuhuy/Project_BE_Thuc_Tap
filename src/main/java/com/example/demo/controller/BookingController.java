package com.example.demo.controller;

import com.example.demo.config.MessageConfig;
import com.example.demo.dto.request.BookingDto;
import com.example.demo.dto.response.ResponMessage;
import com.example.demo.model.*;
import com.example.demo.security.userprincal.UserDetailService;
import com.example.demo.service.booking.IBookingService;
import com.example.demo.service.timeslot.ITimeSlotService;
import com.example.demo.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/bookings")
@CrossOrigin(origins = "*")
public class BookingController {
    @Autowired
    private IBookingService bookingService;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private IUserService userService;
    @Autowired
    ITimeSlotService timeSlotService;
    private final ResponMessage responMessage = MessageConfig.responMessage;

    @GetMapping
    public ResponseEntity<?> showListBooking() {
        List<Booking> bookingList = bookingService.findAll();
        Collections.reverse(bookingList);
        return new ResponseEntity<>(bookingList, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detailBookingById(@PathVariable Long id) {
        Optional<Booking> booking = bookingService.findById(id);
        if (!booking.isPresent()) {
            responMessage.setMessage(MessageConfig.NOT_FOUND);
            return new ResponseEntity<>(responMessage, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }


    @GetMapping("/detail/userBookingByTimeSlotId/{id}")
    public ResponseEntity<?> detailUserBookingByTimeSlotId(@PathVariable Long id) {
        List<Booking> booking = bookingService.findByTimeSlotId(id);
        if (booking.isEmpty()) {
            responMessage.setMessage(MessageConfig.NOT_FOUND);
            return new ResponseEntity<>(responMessage, HttpStatus.NOT_FOUND);
        }
        List<Booking> bookingListLoading = new ArrayList<>();
        for (int i = 0; i < booking.size(); i++) {
            if (booking.get(i).getIsConfirm() == IsConfirm.LOADING || booking.get(i).getIsConfirm() == IsConfirm.ACCEPT){
                    bookingListLoading.add(booking.get(i));
            }
        }
        return new ResponseEntity<>(bookingListLoading, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> createBooking(@RequestBody BookingDto bookingDto, @PathVariable Long id) {
        User user = userDetailService.getCurrentUser();
        if (user.getId() == null){
            responMessage.setMessage(MessageConfig.ACCESS_DENIED);
            return new ResponseEntity<>(responMessage, HttpStatus.OK);
        }
        String role = "";
        role = userService.getUserRole(user);
        if (!role.equals("USER")) {
            responMessage.setMessage(MessageConfig.ACCESS_DENIED_ROLE);
            return new ResponseEntity<>(responMessage, HttpStatus.OK);
        }
        Booking booking = new Booking();
        Date date = new Date();
        booking.setDate_book(date);
        booking.setUser(user);
        booking.setReason(bookingDto.getReason());
        Optional<TimeSlot> timeSlot = timeSlotService.findById(id);
        if (timeSlot.get().isBooked()) {
            responMessage.setMessage(MessageConfig.NOT_TIME_SLOT);
            return new ResponseEntity<>(responMessage, HttpStatus.NOT_FOUND);
        }
        timeSlot.get().setBooked(true);
        booking.setTimeSlot(timeSlot.get());
        timeSlotService.save(timeSlot.get());
        booking.setIsConfirm(IsConfirm.LOADING);
        bookingService.save(booking);
        responMessage.setMessage(MessageConfig.CREATE_SUCCESS);
        return new ResponseEntity<>(responMessage, HttpStatus.OK);
    }

    @PostMapping("/success/booking/oder/timeSlot/{id}")
    public ResponseEntity<?> successBookingsOderById(@PathVariable Long id) {
        Optional<Booking> successBooking = bookingService.findById(id);
        if (!successBooking.isPresent()) {
            responMessage.setMessage(MessageConfig.NOT_FOUND);
            return new ResponseEntity<>(responMessage, HttpStatus.NOT_FOUND);
        }
        successBooking.get().setIsConfirm(IsConfirm.ACCEPT);
        bookingService.save(successBooking.get());
        responMessage.setMessage(MessageConfig.UPDATE_SUCCESS);
        return new ResponseEntity<>(responMessage, HttpStatus.OK);
    }

    @PostMapping("/cancel/booking/oder/timeSlot/{id}")
    public ResponseEntity<?> cancelBookingsOderById(@PathVariable Long id) {
        Optional<Booking> cancelBooking = bookingService.findById(id);
        if (!cancelBooking.isPresent()) {
            responMessage.setMessage(MessageConfig.NOT_FOUND);
            return new ResponseEntity<>(responMessage, HttpStatus.NOT_FOUND);
        }
        Optional<TimeSlot> timeSlot = timeSlotService.findById(cancelBooking.get().getTimeSlot().getId());
        timeSlot.get().setBooked(false);
        cancelBooking.get().setIsConfirm(IsConfirm.NOT_ACCEPT);
        timeSlotService.save(timeSlot.get());
        bookingService.save(cancelBooking.get());
        responMessage.setMessage(MessageConfig.UPDATE_SUCCESS);
        return new ResponseEntity<>(responMessage, HttpStatus.OK);
    }
}
