package com.example.demo.repository;

import com.example.demo.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IDoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByUserId(Long id);
    List<Doctor> getDoctorBySpecialtyId(Long id);
    Optional<Doctor> getDoctorByUserId(Long id);
    @Query(" select d from Doctor d where d.user.name like concat('%', :name , '%') ")
    List<Doctor> listDoctorByName(@Param("name") String name);
}
