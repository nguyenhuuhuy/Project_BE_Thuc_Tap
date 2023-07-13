package com.example.demo.security.userprincal;

import com.example.demo.model.User;
import com.example.demo.repository.IUserRepository;
import com.example.demo.service.user.UserServiceIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    UserServiceIMPL userService;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found -> username or password"+username));
        return UserPrinciple.build(user);
    }
    public User getCurrentUser(){
        Optional<User> user;
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        if(userRepository.existsByUsername(userName)){
            user = userService.findByUsername(userName);
        } else {
            user = Optional.of(new User());
            user.get().setUsername("Anonymous");
        }
        return user.get();
    }
}
