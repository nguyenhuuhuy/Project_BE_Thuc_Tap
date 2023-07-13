package com.example.demo.repository;

import com.example.demo.model.Doctor;
import com.example.demo.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISpecialtyRepository extends JpaRepository<Specialty, Long> {
    boolean existsByName(String name);
    List<Specialty> findByNameContaining(String name);
}
