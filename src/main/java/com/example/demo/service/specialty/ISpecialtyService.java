package com.example.demo.service.specialty;

import com.example.demo.model.Specialty;
import com.example.demo.service.IGenericService;

import java.util.List;

public interface ISpecialtyService extends IGenericService<Specialty> {
    boolean existsByName(String name);
    List<Specialty> findByNameContaining(String name);

}
