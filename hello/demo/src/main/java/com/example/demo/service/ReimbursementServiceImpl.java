package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.Reimbursement;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.ReimbursementRepository;
import com.example.demo.dto.response.TicketRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class ReimbursementServiceImpl {


    @Autowired
    @Lazy
    ReimbursementRepository reimbursementRepository;

    @Autowired
    @Lazy
    UserRepository userRepository;

    @Autowired
    @Lazy
    JwtService jwtService;

    @Transactional
    public Reimbursement createReimbursement(String token, Reimbursement reimbursement) {
        int id = jwtService.getIdFromToken(token);
        Optional<User> userOptional = userRepository.findById(id);
        
        if(userOptional.isEmpty() || 
        reimbursement.getDescription().length() > 255 || 
        reimbursement.getAmount() < 0)  return null;

        reimbursement.setUserID(userOptional.get());
        return reimbursementRepository.save(reimbursement);
    }

    @Transactional
    public Reimbursement updateReimbursement(String token, TicketRequest reimbursement) {
        String role = jwtService.getRoleFromToken(token);
        String status = reimbursement.getStatus();

        Optional<Reimbursement> reimbursementOptional = reimbursementRepository.findById(reimbursement.getReimbursementid());
        if(reimbursementOptional.isPresent() &&
         (status.equals("Pending") || status.equals("Approved") || status.equals("Denied")) &&
          role.equals("Manager")) {
            Reimbursement reimbursementEdit = reimbursementOptional.get();
            reimbursementEdit.setStatus(status);
            return reimbursementRepository.save(reimbursementEdit);
        } 
        return null;
    }

    @Transactional
    public List<Reimbursement> getAllReimbursements() {
        return reimbursementRepository.findAll();
    }

    @Transactional
    public List<Reimbursement> getAllPendingReimbursements() {
        return reimbursementRepository.findByStatus("Pending");
    }
}
