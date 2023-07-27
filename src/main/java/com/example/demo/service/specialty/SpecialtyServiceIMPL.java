package com.example.demo.service.specialty;

import com.example.demo.model.Specialty;
import com.example.demo.repository.ISpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class SpecialtyServiceIMPL implements ISpecialtyService{
@Autowired
    ISpecialtyRepository iSpecialtyRepository;

    @Override
    public List<Specialty> findAll() {
        return iSpecialtyRepository.findAll();
    }

    @Override
    public void save(Specialty specialty) {
        iSpecialtyRepository.save(specialty);
    }

    @Override
    public Page<Specialty> findAll(Pageable pageable) {
        return iSpecialtyRepository.findAll(pageable);
    }

    @Override
    public Optional<Specialty> findById(Long id) {
        return iSpecialtyRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        iSpecialtyRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return iSpecialtyRepository.existsByName(name);
    }

    @Override
    public List<Specialty> findByNameContaining(String name) {
        return iSpecialtyRepository.findByNameContaining(name);
    }
}
