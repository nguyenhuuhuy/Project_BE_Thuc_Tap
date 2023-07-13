package com.example.demo.repository;

import com.example.demo.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByUserId(Long id);
    List<Doctor> getDoctorBySpecialtyId(Long id);
    Doctor getDoctorByUserId(Long id);
}
