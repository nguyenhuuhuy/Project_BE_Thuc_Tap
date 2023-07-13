package com.example.demo.service.doctor;

import com.example.demo.model.Doctor;
import com.example.demo.service.IGenericService;

import java.util.List;

public interface IDoctorService extends IGenericService<Doctor> {
    boolean existsByUserId(Long id);
    List<Doctor> getDoctorBySpecialtyId(Long id);
    Doctor getDoctorByUserId(Long id);


}
