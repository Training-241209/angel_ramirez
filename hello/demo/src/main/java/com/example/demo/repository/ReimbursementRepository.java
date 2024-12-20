package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.Reimbursement;

@Repository
public interface ReimbursementRepository extends JpaRepository<Reimbursement, Integer>{

    List<Reimbursement> findByStatus(String string);
}
