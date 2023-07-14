package com.example.demo.controller;

import com.example.demo.config.MessageConfig;
import com.example.demo.dto.request.ChangeAvatar;
import com.example.demo.dto.request.SignInForm;
import com.example.demo.dto.request.SignUpForm;
import com.example.demo.dto.request.UserDto;
import com.example.demo.dto.response.JwtResponse;
import com.example.demo.dto.response.ResponMessage;
import com.example.demo.model.*;
import com.example.demo.security.jwt.JwtProvider;
import com.example.demo.security.jwt.JwtTokenFilter;
import com.example.demo.security.userprincal.UserDetailService;
import com.example.demo.security.userprincal.UserPrinciple;
import com.example.demo.service.booking.IBookingService;
import com.example.demo.service.doctor.IDoctorService;
import com.example.demo.service.role.RoleServiceIMPL;
import com.example.demo.service.specialty.ISpecialtyService;
import com.example.demo.service.timeslot.ITimeSlotService;
import com.example.demo.service.user.UserServiceIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RequestMapping("/api")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    @Autowired
    UserServiceIMPL userService;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private IBookingService bookingService;
    @Autowired
    ISpecialtyService iSpecialtyService;
    @Autowired
    IDoctorService doctorService;
    @Autowired
    ITimeSlotService timeSlotService;
    @Autowired
    RoleServiceIMPL roleService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    JwtTokenFilter jwtTokenFilter;
    private final ResponMessage responMessage = MessageConfig.responMessage;
    @GetMapping("/doctors")
    public ResponseEntity<?> showListDoctor() {
        List<Doctor> storyList = doctorService.findAll();
        Collections.reverse(storyList);
        return new ResponseEntity<>(storyList, HttpStatus.OK);
    }
    @GetMapping("/doctors/timeSlotByDoctor/{id}")
    public ResponseEntity<?> showListTimeSlotByDoctorId(@PathVariable Long id){
        List<TimeSlot> timeSlotList = timeSlotService.getTimeSlotsByDoctorId(id);
        if (timeSlotList.isEmpty()){
            responMessage.setMessage(MessageConfig.NOT_FOUND);
            return new ResponseEntity<>(responMessage,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(timeSlotList,HttpStatus.OK);
    }

    @GetMapping("/detail/timeSlotById/{id}")
    public ResponseEntity<?> detailTimeSlotById(@PathVariable Long id){
        Optional<TimeSlot> timeSlot = timeSlotService.findById(id);
        if (!timeSlot.isPresent()){
            responMessage.setMessage(MessageConfig.NOT_FOUND);
            return new ResponseEntity<>(responMessage,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(timeSlot,HttpStatus.OK);
    }

    @GetMapping("/list/oderByUserId")
    public ResponseEntity<?> showListOderByUserId(HttpServletRequest request){
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
        List<Booking> bookingOderByUserId = bookingService.findByUserId(user.getId());
        if (bookingOderByUserId.isEmpty()){
            responMessage.setMessage(MessageConfig.NOT_FOUND);
            return new ResponseEntity<>(responMessage,HttpStatus.OK);
        }
        List<Booking> bookingListOderLoading = new ArrayList<>();
        for (int i = 0; i < bookingOderByUserId.size(); i++) {
            if (bookingOderByUserId.get(i).getIsConfirm() == IsConfirm.LOADING){
                bookingListOderLoading.add(bookingOderByUserId.get(i));
            }
        }
        return new ResponseEntity<>(bookingListOderLoading,HttpStatus.OK);
    }

    @GetMapping("/list/cancelByUserId")
    public ResponseEntity<?> showListCancelByUserId(HttpServletRequest request){
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
        List<Booking> bookingOderByUserId = bookingService.findByUserId(user.getId());
        if (bookingOderByUserId.isEmpty()){
            responMessage.setMessage(MessageConfig.NOT_FOUND);
            return new ResponseEntity<>(responMessage,HttpStatus.OK);
        }
        List<Booking> bookingListOderCancel = new ArrayList<>();
        for (int i = 0; i < bookingOderByUserId.size(); i++) {
            if (bookingOderByUserId.get(i).getIsConfirm() == IsConfirm.CANCEL){
                bookingListOderCancel.add(bookingOderByUserId.get(i));
            }
        }
        return new ResponseEntity<>(bookingListOderCancel,HttpStatus.OK);
    }

    @GetMapping("/list/acceptByUserId")
    public ResponseEntity<?> showListAcceptByUserId(HttpServletRequest request){
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
        List<Booking> bookingOderByUserId = bookingService.findByUserId(user.getId());
        if (bookingOderByUserId.isEmpty()){
            responMessage.setMessage(MessageConfig.NOT_FOUND);
            return new ResponseEntity<>(responMessage,HttpStatus.OK);
        }
        List<Booking> bookingListOderAccept = new ArrayList<>();
        for (int i = 0; i < bookingOderByUserId.size(); i++) {
            if (bookingOderByUserId.get(i).getIsConfirm() == IsConfirm.ACCEPT){
                bookingListOderAccept.add(bookingOderByUserId.get(i));
            }
        }
        return new ResponseEntity<>(bookingListOderAccept,HttpStatus.OK);
    }
    @GetMapping("/specialty")
    public ResponseEntity<?> showListSpecialty() {
        List<Specialty> specialtyList = iSpecialtyService.findAll();
        Collections.reverse(specialtyList);
        return new ResponseEntity<>(specialtyList, HttpStatus.OK);
    }
    @GetMapping("/users")
    public ResponseEntity<?> getListUser() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/detail/user/{id}")
    public ResponseEntity<?> detailUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (!user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpForm signUpForm) {
        if (userService.existsByUsername(signUpForm.getUsername())) {
            responMessage.setMessage(MessageConfig.NAME_ALREADY_EXISTS);
            return new ResponseEntity<>(responMessage, HttpStatus.OK);
        }
        if (userService.existsByEmail(signUpForm.getEmail())) {
            responMessage.setMessage(MessageConfig.EMAIL_ALREADY_EXISTS);
            return new ResponseEntity<>(responMessage, HttpStatus.OK);
        }
        User user = new User(signUpForm.getName(), signUpForm.getUsername(), signUpForm.getEmail(), passwordEncoder.encode(signUpForm.getPassword()));
        Set<String> strRoles = signUpForm.getRoles();
        Set<Role> roles = new HashSet<>();
        strRoles.forEach(role -> {
            switch (role) {
                case MessageConfig.ADMIN:
                    Role adminRole = roleService.findByName(RoleName.ADMIN).orElseThrow(
                            () -> new RuntimeException("Role not found")
                    );
                    roles.add(adminRole);
                    break;
                case MessageConfig.PM:
                    Role pmRole = roleService.findByName(RoleName.PM).orElseThrow(() -> new RuntimeException("Role not found"));
                    roles.add(pmRole);
                    break;
                case MessageConfig.DOCTOR:
                    Role doctorRole = roleService.findByName(RoleName.DOCTOR).orElseThrow(() -> new RuntimeException("Role not found"));
                    roles.add(doctorRole);
                    break;
                default:
                    Role userRole = roleService.findByName(RoleName.USER).orElseThrow(() -> new RuntimeException("Role not found"));
                    roles.add(userRole);
            }
        });
        user.setRoles(roles);
        userService.save(user);
        responMessage.setMessage(MessageConfig.CREATE_SUCCESS);
        return new ResponseEntity<>(responMessage, HttpStatus.OK);
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> login(@Valid @RequestBody SignInForm signInForm) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInForm.getUsername(), signInForm.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.createToken(authentication);
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(token, userPrinciple.getName(), userPrinciple.getAvatar(), userPrinciple.getAuthorities()));
    }

    @PutMapping("/change/avatars")
    public ResponseEntity<?> changeAvatar(HttpServletRequest request, @Valid @RequestBody ChangeAvatar changeAvatar) {
        String token = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUerNameFromToken(token);
        User user = userService.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        if (changeAvatar.getAvatar() == null || changeAvatar.getAvatar().trim().equals("")) {
            responMessage.setMessage(MessageConfig.NO_CHANGE);
            return new ResponseEntity<>(responMessage, HttpStatus.OK);
        } else {
            user.setAvatar(changeAvatar.getAvatar());
            userService.save(user);
            responMessage.setMessage(MessageConfig.UPDATE_SUCCESS);
            return new ResponseEntity<>(responMessage, HttpStatus.OK);
        }
    }

    @PutMapping("/update/users")
    public ResponseEntity<?> updateUser(HttpServletRequest request, @Valid @RequestBody UserDto userDto) {
        String token = jwtTokenFilter.getJwt(request);
        if (token == null) {
            responMessage.setMessage(MessageConfig.NO_USER);
            return new ResponseEntity<>(responMessage, HttpStatus.OK);
        }
        String username = jwtProvider.getUerNameFromToken(token);
        User user = userService.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user name not fond"));
        if (user.isStatus()) {
            responMessage.setMessage(MessageConfig.ACCESS_DENIED);
            return new ResponseEntity<>(responMessage, HttpStatus.OK);
        }
        if (userDto.getAvatar() == null || userDto.getAvatar().trim().equals("")) {
            responMessage.setMessage(MessageConfig.AVATAR_FAILED);
            return new ResponseEntity<>(responMessage, HttpStatus.OK);
        } else {
            user.setName(userDto.getName());
            user.setAvatar(userDto.getAvatar());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userService.save(user);
            responMessage.setMessage(MessageConfig.UPDATE_SUCCESS);
            return new ResponseEntity<>(responMessage, HttpStatus.OK);
        }
    }

    @GetMapping("/search/users/{search}")
    public ResponseEntity<?> searchSpecialty(@PathVariable String search) {
        List<User> userList = userService.findByNameContaining(search);
        if (userList.isEmpty()) {
            responMessage.setMessage(MessageConfig.NOT_FOUND);
            return new ResponseEntity<>(responMessage, HttpStatus.OK);
        }
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }
}
