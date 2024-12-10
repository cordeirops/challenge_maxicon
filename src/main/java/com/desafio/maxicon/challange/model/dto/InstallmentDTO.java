package com.desafio.maxicon.challange.model.dto;

import com.desafio.maxicon.challange.model.loan.Loan;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "loan_installments")
@NoArgsConstructor
public class InstallmentDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    @JsonBackReference
    private Loan loan;

    private int installmentNumber;
    private BigDecimal interest;
    private BigDecimal amortization;
    private BigDecimal installmentValue;
    private BigDecimal remainingBalance;

}
