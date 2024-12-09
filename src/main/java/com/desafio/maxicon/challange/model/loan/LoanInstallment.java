package com.desafio.maxicon.challange.model.loan;

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
public class LoanInstallment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    @JsonBackReference  // Evita a serialização infinita entre LoanPrice e LoanInstallment
    private LoanPrice loanPrice;

    private int installmentNumber; // Número da parcela
    private BigDecimal interest; // Valor dos juros
    private BigDecimal amortization; // Valor da amortização
    private BigDecimal installmentValue; // Valor total da parcela (PMT)
    private BigDecimal remainingBalance;// Saldo devedor após o pagamento da parcela

}
