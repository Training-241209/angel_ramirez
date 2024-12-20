package com.example.demo.service;

import com.example.demo.entity.User;

import com.example.demo.repository.UserRepository;
import com.example.demo.repository.ReimbursementRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import org.mindrot.jbcrypt.BCrypt;

@Service
public class UserServiceImpl {

    private final String salt = BCrypt.gensalt();

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReimbursementRepository reimbursementRepository;

    @Autowired
    JwtService jwtService;
    
    @Transactional
    public User registerUser(User user) { 
        if(user.getUsername() == "" || user.getPassword().length() < 4 || 
        userRepository.findUserByUsername(user.getUsername()) != null) {
            return null;
        }
        String hashedPassword = hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        User fin = userRepository.save(user);
        userRepository.flush();
        return fin;
    }

    public String loginUser(String username, String password) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findUserByUsername(username));
        if(userOptional.isPresent() && hashPassword(password).equals(userOptional.get().getPassword())) {
            userOptional.get().getPassword();
            String jwt = jwtService.generateToken(userOptional.get());
            return jwt;
        }
        return null;
    }
    
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, salt);
    }
}
