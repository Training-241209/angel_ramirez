package com.example.demo.controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.Lazy;

import com.example.demo.entity.User;
import com.example.demo.dto.response.LoginRequest;
import com.example.demo.entity.Reimbursement;

import com.example.demo.service.ReimbursementServiceImpl;

import com.example.demo.service.UserServiceImpl;
import com.example.demo.service.JwtService;
import com.example.demo.dto.response.TicketRequest;


@RestController
public class ERSController {
    
    @Autowired
    @Lazy
    private UserServiceImpl userService;
    
    @Autowired
    @Lazy
    private ReimbursementServiceImpl reimbursementService;

    @Autowired
    JwtService jwtService;

    @SuppressWarnings("rawtypes")
    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody User user) {

        Optional<User> userOptional = Optional.ofNullable(userService.registerUser(user));
        if(userOptional.isPresent()) {
            return ResponseEntity.status(200).body(userOptional.get().toString());
        }
        return ResponseEntity.status(403).body(null);
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody LoginRequest loginRequest) {
        Optional<String> tokenOptional = Optional.ofNullable(userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword())); 
        
        if(tokenOptional.isPresent()) {
            String jwt = tokenOptional.get();
            return ResponseEntity.status(200).body(jwt);
        }
        return ResponseEntity.status(401).body(null);
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/reimbursement")
    public ResponseEntity createTicket(@RequestHeader(name="authorization") String token, @RequestBody Reimbursement ticket) {

        Optional<Reimbursement> reimbursementOptional = Optional.ofNullable(reimbursementService.createReimbursement(token, ticket));

        if(reimbursementOptional.isPresent()) { 
            return ResponseEntity.status(200).body(reimbursementOptional.toString());
        }
        return ResponseEntity.status(401).body(null);
    }

    @SuppressWarnings("rawtypes")
    @PatchMapping("/reimbursement/edit")
    public ResponseEntity editTicket(@RequestHeader(name="authorization") String token, @RequestBody TicketRequest ticket) {

        System.out.println(ticket.toString());

        Optional<Reimbursement> reimbursementOptional = Optional.ofNullable(reimbursementService.updateReimbursement(token, ticket));
        if(reimbursementOptional.isPresent()) { 
            return ResponseEntity.status(200).body(reimbursementOptional.get().toString());
        }
        return ResponseEntity.status(401).body(null);
    }
    
}
