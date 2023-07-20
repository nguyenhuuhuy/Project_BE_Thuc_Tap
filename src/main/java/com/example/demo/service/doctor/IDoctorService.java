package com.example.demo.service.doctor;

import com.example.demo.model.Doctor;
import com.example.demo.service.IGenericService;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IDoctorService extends IGenericService<Doctor> {
    boolean existsByUserId(Long id);
    List<Doctor> getDoctorBySpecialtyId(Long id);
    Optional<Doctor> getDoctorByUserId(Long id);
    List<Doctor> listDoctorByName(@Param("name") String name);

}
