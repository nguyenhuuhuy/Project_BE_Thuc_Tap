package com.example.demo.service.user;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceIMPL implements IUserService {
    @Autowired
    IUserRepository userRepository;
    @Override
    public Optional<User> findByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findByNameContaining(String name) {
        return userRepository.findByNameContaining(name);
    }

    @Override
    public String getUserRole(User user) {
        String strRole = "USER";
        List<Role> roleList = new ArrayList<>();
        user.getRoles().forEach(role -> {
            roleList.add(role);
        });
        for (int i = 0; i < roleList.size(); i++) {
            if (roleList.get(i).getName().name().equals("ADMIN")){
                strRole= "ADMIN";
                return strRole;
            }
            if (roleList.get(i).getName().name().equals("PM")){
                strRole="PM";
            }
            if (roleList.get(i).getName().name().equals("DOCTOR")){
                strRole="DOCTOR";
            }
        }
        return strRole;
    }
}
