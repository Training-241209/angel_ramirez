package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name="reimbursement")
public class Reimbursement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer reimbursementId;
    
    private String description;
    
    private Double amount;
    
    private String status;
    
    @ManyToOne
    @JoinColumn(name="userid")
    @JsonBackReference
    private User userID;

    public Reimbursement(String description, Double amount, String status){
        this.description = description;
        this.amount = amount;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reimbursement{" +
                "reimbursementId=" + reimbursementId +
                ", description='" + description + '\'' +
                ", amount='" + amount + '\'' +
                ", status='" + status + '\'' +
                ", userID='" + userID + '\'' +
                '}';
    }
}
