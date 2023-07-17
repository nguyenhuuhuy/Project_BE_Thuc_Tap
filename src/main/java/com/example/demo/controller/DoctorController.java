package com.example.demo.controller;

import com.example.demo.config.MessageConfig;
import com.example.demo.dto.request.DoctorDto;
import com.example.demo.dto.response.ResponMessage;
import com.example.demo.model.*;
import com.example.demo.security.jwt.JwtProvider;
import com.example.demo.security.jwt.JwtTokenFilter;
import com.example.demo.security.userprincal.UserDetailService;
import com.example.demo.service.booking.IBookingService;
import com.example.demo.service.doctor.IDoctorService;
import com.example.demo.service.role.IRoleService;
import com.example.demo.service.specialty.ISpecialtyService;
import com.example.demo.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {
    @Autowired
    private IDoctorService doctorService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private IBookingService bookingService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ISpecialtyService specialtyService;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    JwtTokenFilter jwtTokenFilter;
    private final ResponMessage responMessage = MessageConfig.responMessage;
    @GetMapping
    public ResponseEntity<?> showListDoctor() {
        List<Doctor> storyList = doctorService.findAll();
        Collections.reverse(storyList);
        return new ResponseEntity<>(storyList, HttpStatus.OK);
    }
    @GetMapping("/detail/doctor")
    public ResponseEntity<?> detailDoctor(HttpServletRequest request){
        String token = jwtTokenFilter.getJwt(request);
        if (token == null){
            responMessage.setMessage(MessageConfig.NO_USER);
            return new ResponseEntity<>(responMessage,HttpStatus.OK);
        }
        String username = jwtProvider.getUerNameFromToken(token);
        User user = userService.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        if (user.getId() == null){
            responMessage.setMessage(MessageConfig.NO_USER);
            return new ResponseEntity<>(responMessage,HttpStatus.OK);
        }
        Optional<Doctor> doctor = doctorService.getDoctorByUserId(user.getId());
        if (!doctor.isPresent()){
            responMessage.setMessage(MessageConfig.NOT_FOUND);
            return new ResponseEntity<>(responMessage,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(doctor,HttpStatus.OK);
    }
    @GetMapping("/detail-doctor/{id}")
    public ResponseEntity<?> detailDoctorById(@PathVariable Long id){
        Optional<Doctor> doctor = doctorService.findById(id);
        if (!doctor.isPresent()){
            responMessage.setMessage(MessageConfig.NOT_FOUND);
            return new ResponseEntity<>(responMessage,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(doctor,HttpStatus.OK);
    }
    @GetMapping("/listDoctorBySpecialtyId/{id}")
    public ResponseEntity<?> listDoctorBySpecialtyId(@PathVariable Long id){
        List<Doctor> doctorList = doctorService.getDoctorBySpecialtyId(id);
        return new ResponseEntity<>(doctorList,HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?> createDoctor(@RequestBody DoctorDto doctorDto) {
        User user = userDetailService.getCurrentUser();
        Doctor doctor = new Doctor();
        Optional<Specialty> specialty  = specialtyService.findById(doctorDto.getSpecialty().getId());
        if (!specialty.isPresent()){
            responMessage.setMessage(MessageConfig.NOT_FOUND);
            return new ResponseEntity<>(responMessage,HttpStatus.NOT_FOUND);
        }
        doctor.setSpecialty(doctorDto.getSpecialty());
        if (doctorService.existsByUserId(user.getId())){
            responMessage.setMessage(MessageConfig.NAME_EXISTED);
            return new ResponseEntity<>(responMessage,HttpStatus.OK);
        }
        Set<Role> roleSet = new HashSet<>();
        Role pmRole = roleService.findByName(RoleName.DOCTOR).orElseThrow(() -> new RuntimeException("Role not found"));
        roleSet.add(pmRole);
        user.setRoles(roleSet);
        doctor.setUser(user);
        userService.save(user);
        doctorService.save(doctor);
        responMessage.setMessage(MessageConfig.CREATE_SUCCESS);
        return new ResponseEntity<>(responMessage,HttpStatus.OK);
    }
    @PutMapping("/approve-doctor/{id}")
    public ResponseEntity<?> blockDoctorById(@PathVariable Long id){
        Optional<Doctor> doctor = doctorService.findById(id);
        if (!doctor.isPresent()){
            responMessage.setMessage(MessageConfig.NOT_FOUND);
            return new ResponseEntity<>(responMessage,HttpStatus.NOT_FOUND);
        }
        if (doctor.get().isWork()){
            doctor.get().setWork(false);
            doctorService.save(doctor.get());
            responMessage.setMessage(MessageConfig.BLOCK_SUCCESS);
            return new ResponseEntity<>(responMessage,HttpStatus.OK);
        }
        doctor.get().setWork(true);
        doctorService.save(doctor.get());
        responMessage.setMessage(MessageConfig.UN_BLOCK_SUCCESS);
        return new ResponseEntity<>(responMessage,HttpStatus.OK);
    }
    @PutMapping("/update-doctor/{id}")
    public ResponseEntity<?> updateDoctorById(@PathVariable Long id, @RequestBody DoctorDto doctorDto){
        Optional<Doctor> doctor = doctorService.findById(id);
        if (!doctor.isPresent()){
            responMessage.setMessage(MessageConfig.NOT_FOUND);
            return new ResponseEntity<>(responMessage,HttpStatus.NOT_FOUND);
        }
        if (doctor.get().getSpecialty().getId().equals(doctorDto.getSpecialty().getId())){
            responMessage.setMessage(MessageConfig.NO_CHANGE);
            return new ResponseEntity<>(responMessage,HttpStatus.OK);
        }
        doctor.get().setSpecialty(doctorDto.getSpecialty());
        doctorService.save(doctor.get());
        responMessage.setMessage(MessageConfig.UPDATE_SUCCESS);
        return new ResponseEntity<>(responMessage,HttpStatus.OK);
    }
//    @PutMapping("success/booking/oder/timeSlot/{id}")
//    public ResponseEntity<?> successUserOderByTimeSlotId(@PathVariable Long id) {
//        Optional<Booking> booking = bookingService.findById(id);
//        if (!booking.isPresent()){
//            responMessage.setMessage(MessageConfig.NOT_FOUND);
//            return new ResponseEntity<>(responMessage,HttpStatus.NOT_FOUND);
//        }
//        booking.get().setIsConfirm(IsConfirm.ACCEPT);
//        bookingService.save(booking.get());
//        responMessage.setMessage(MessageConfig.UPDATE_SUCCESS);
//        return new ResponseEntity<>(responMessage,HttpStatus.OK);
//    }
}
