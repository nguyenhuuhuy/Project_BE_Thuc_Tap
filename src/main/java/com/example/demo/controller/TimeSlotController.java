package com.example.demo.controller;
import com.example.demo.config.MessageConfig;
import com.example.demo.dto.request.TimeSlotDto;
import com.example.demo.dto.response.ResponMessage;
import com.example.demo.model.Doctor;
import com.example.demo.model.TimeSlot;
import com.example.demo.model.User;
import com.example.demo.security.userprincal.UserDetailService;
import com.example.demo.service.doctor.IDoctorService;
import com.example.demo.service.timeslot.ITimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/timeSlots")
@CrossOrigin(origins = "*")
public class TimeSlotController {
    @Autowired
    ITimeSlotService timeSlotService;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private IDoctorService doctorService;
    private final ResponMessage responMessage = MessageConfig.responMessage;
    @GetMapping
    public ResponseEntity<?> showListTimeSlot(){
        List<TimeSlot> timeSlotList = timeSlotService.findAll();
        Collections.reverse(timeSlotList);
        return new ResponseEntity<>(timeSlotList, HttpStatus.OK);
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
    @PostMapping
    public ResponseEntity<?> createTimeSlot(@RequestBody TimeSlotDto timeSlotDto){
        User user = userDetailService.getCurrentUser();
        Doctor doctor = doctorService.getDoctorByUserId(user.getId());
        TimeSlot timeSlotData = new TimeSlot();
        timeSlotData.setDate_book(timeSlotDto.getDate_book());
        switch (timeSlotDto.getTime_book()){
            case MessageConfig.S1:
                timeSlotData.setTimes(MessageConfig.S1);
                break;
            case MessageConfig.S2:
                timeSlotData.setTimes(MessageConfig.S2);
                break;
            case MessageConfig.S3:
                timeSlotData.setTimes(MessageConfig.S3);
                break;
            case MessageConfig.C1:
                timeSlotData.setTimes(MessageConfig.C1);
                break;
            case MessageConfig.C2:
                timeSlotData.setTimes(MessageConfig.C2);
                break;
            case MessageConfig.C3:
                timeSlotData.setTimes(MessageConfig.C3);
                break;
            default:
                responMessage.setMessage(MessageConfig.NOT_FOUND);
                return new ResponseEntity<>(responMessage,HttpStatus.NOT_FOUND);
        }
        timeSlotData.setDoctor(doctor);
        List<TimeSlot> timeSlotList = timeSlotService.getTimeSlotsByDoctorId(doctor.getId());
        for (TimeSlot timeSlot : timeSlotList) {
            if (timeSlot.getDate_book().getDay() == timeSlotData.getDate_book().getDay()) {
                if (timeSlot.getTimes().equals(timeSlotData.getTimes())){
                    responMessage.setMessage(MessageConfig.NOT_FOUND);
                    return new ResponseEntity<>(responMessage,HttpStatus.NOT_FOUND);
                }
            }
        }
        timeSlotService.save(timeSlotData);
        responMessage.setMessage(MessageConfig.CREATE_SUCCESS);
        return new ResponseEntity<>(responMessage,HttpStatus.OK);
    }


}
