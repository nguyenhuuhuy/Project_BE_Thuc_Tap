package com.example.demo.service.doctor;

import com.example.demo.model.Doctor;
import com.example.demo.repository.IDoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class DoctorServiceIMPL implements IDoctorService{
    @Autowired
    IDoctorRepository iDoctorRepository;

    @Override
    public List<Doctor> findAll() {
        return iDoctorRepository.findAll();
    }

    @Override
    public void save(Doctor doctor) {
        iDoctorRepository.save(doctor);
    }

    @Override
    public Page<Doctor> findAll(Pageable pageable) {
        return iDoctorRepository.findAll(pageable);
    }

    @Override
    public Optional<Doctor> findById(Long id) {
        return iDoctorRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        iDoctorRepository.deleteById(id);
    }

    @Override
    public boolean existsByUserId(Long id) {
        return iDoctorRepository.existsByUserId(id);
    }

    @Override
    public List<Doctor> getDoctorBySpecialtyId(Long id) {
        return iDoctorRepository.getDoctorBySpecialtyId(id);
    }

    @Override
    public Optional<Doctor> getDoctorByUserId(Long id) {
        return iDoctorRepository.getDoctorByUserId(id);
    }

    @Override
    public List<Doctor> listDoctorByName(String name) {
        return iDoctorRepository.listDoctorByName(name);
    }
}
