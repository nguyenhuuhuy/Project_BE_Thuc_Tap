package com.example.demo.controller;

import com.example.demo.config.MessageConfig;
import com.example.demo.dto.response.ResponMessage;
import com.example.demo.model.Specialty;
import com.example.demo.service.specialty.ISpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/specialty")
@CrossOrigin(origins = "*")
public class SpecialtyController {
    @Autowired
    ISpecialtyService iSpecialtyService;
    private final ResponMessage responMessage = MessageConfig.responMessage;
    @GetMapping
    public ResponseEntity<?> showListSpecialty() {
        List<Specialty> specialtyList = iSpecialtyService.findAll();
        Collections.reverse(specialtyList);
        return new ResponseEntity<>(specialtyList, HttpStatus.OK);
    }
    @GetMapping("/detail/specialty/{id}")
    public ResponseEntity<?> detailSpecialtyById(@PathVariable Long id){
        Optional<Specialty> specialty = iSpecialtyService.findById(id);
        if (!specialty.isPresent()){
            responMessage.setMessage(MessageConfig.NOT_FOUND);
            return new ResponseEntity<>(responMessage,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(specialty,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createSpecialty(@RequestBody Specialty specialty){
        if (iSpecialtyService.existsByName(specialty.getName())){
            responMessage.setMessage(MessageConfig.NAME_EXISTED);
        } else {
            iSpecialtyService.save(specialty);
            responMessage.setMessage(MessageConfig.CREATE_SUCCESS);
        }
        return new ResponseEntity<>(responMessage,HttpStatus.OK);
    }
    @PutMapping("/update/specialty/{id}")
    public ResponseEntity<?> updateSpecialty(@PathVariable Long id,@RequestBody Specialty specialty){
        Optional<Specialty> specialty1 =iSpecialtyService.findById(id);
        if (!specialty1.isPresent()) {
            responMessage.setMessage(MessageConfig.NOT_FOUND);
            return new ResponseEntity<>(responMessage,HttpStatus.NOT_FOUND);
        }
        if (iSpecialtyService.existsByName(specialty.getName())){
            responMessage.setMessage(MessageConfig.NAME_EXISTED);
            return new ResponseEntity<>(responMessage,HttpStatus.OK);
        }
        if (specialty.getName().equals(specialty1.get().getName())){
            responMessage.setMessage(MessageConfig.NO_CHANGE);
            return new ResponseEntity<>(responMessage,HttpStatus.OK);
        }
        specialty.setId(specialty1.get().getId());
        iSpecialtyService.save(specialty);
        responMessage.setMessage(MessageConfig.UPDATE_SUCCESS);
        return new ResponseEntity<>(responMessage,HttpStatus.OK);
    }
    @DeleteMapping("/delete-specialty/{id}")
    public ResponseEntity<?> deleteSpecialtyById(@PathVariable Long id){
        Optional<Specialty> specialty = iSpecialtyService.findById(id);
        if (!specialty.isPresent()){
            responMessage.setMessage(MessageConfig.NOT_FOUND);
            return new ResponseEntity<>(responMessage,HttpStatus.NOT_FOUND);
        }
        iSpecialtyService.deleteById(id);
        responMessage.setMessage(MessageConfig.DELETE_SUCCESS);
        return new ResponseEntity<>(responMessage,HttpStatus.OK);
    }
    @GetMapping("/search/specialty/{search}")
    public ResponseEntity<?> searchSpecialty(@PathVariable String search){
        List<Specialty> specialtyList = iSpecialtyService.findByNameContaining(search);
        if (specialtyList.isEmpty()){
            responMessage.setMessage(MessageConfig.NOT_FOUND);
            return new ResponseEntity<>(responMessage, HttpStatus.OK);
        }
        return new ResponseEntity<>(specialtyList,HttpStatus.OK);
    }
}
