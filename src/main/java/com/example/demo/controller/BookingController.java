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

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/detail-booking/{id}")
    public ResponseEntity<?> detailBookingById(@PathVariable Long id) {
        Optional<Booking> booking = bookingService.findById(id);
        if (!booking.isPresent()) {
            responMessage.setMessage(MessageConfig.NOT_FOUND);
            return new ResponseEntity<>(responMessage, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }


    @GetMapping("/detail/userBookingByTimeSlotId/{id}")
    public ResponseEntity<?> detailUserBookingByTimeSlotId(@PathVariable Long id){
        List<Booking> booking = bookingService.findByTimeSlotId(id);
        if (booking.isEmpty()){
            responMessage.setMessage(MessageConfig.NOT_FOUND);
            return new ResponseEntity<>(responMessage,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(booking,HttpStatus.OK);
    }
    @PostMapping("/{id}")
    public ResponseEntity<?> createBooking(@RequestBody BookingDto bookingDto,@PathVariable Long id) {
        User user = userDetailService.getCurrentUser();
        String role = "";
        role = userService.getUserRole(user);
        if (!role.equals("USER")) {
            responMessage.setMessage(MessageConfig.ACCESS_DENIED);
            return new ResponseEntity<>(responMessage, HttpStatus.OK);
        }
        Booking booking = new Booking();
        Date date = new Date();
        booking.setDate_book(date);
        booking.setUser(user);
        booking.setReason(bookingDto.getReason());
        Optional<TimeSlot> timeSlot = timeSlotService.findById(id);
        if (timeSlot.get().isBooked()){
            responMessage.setMessage(MessageConfig.NOT_TIME_SLOT);
            return new ResponseEntity<>(responMessage,HttpStatus.NOT_FOUND);
        }
        timeSlot.get().setBooked(true);
        booking.setTimeSlot(timeSlot.get());
        timeSlotService.save(timeSlot.get());
        booking.setIsConfirm(IsConfirm.LOADING);
        bookingService.save(booking);
        responMessage.setMessage(MessageConfig.CREATE_SUCCESS);
        return new ResponseEntity<>(responMessage, HttpStatus.OK);
    }

    @PutMapping("/timeslot/doctor/{id}")
    public ResponseEntity<?> successBookingsOderById(@PathVariable Long id){
        Optional<Booking> successBooking = bookingService.findById(id);
        if (!successBooking.isPresent()){
            responMessage.setMessage(MessageConfig.NOT_FOUND);
            return new ResponseEntity<>(responMessage,HttpStatus.NOT_FOUND);
        }
        successBooking.get().setIsConfirm(IsConfirm.ACCEPT);
        bookingService.save(successBooking.get());
        responMessage.setMessage(MessageConfig.UPDATE_SUCCESS);
        return new ResponseEntity<>(responMessage,HttpStatus.OK);
    }
}
