package com.desafio.maxicon.challange.model.loan;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanInstallment {
    private int installmentNumber; // Número da parcela
    private BigDecimal interest; // Valor dos juros
    private BigDecimal amortization; // Valor da amortização
    private BigDecimal installmentValue; // Valor total da parcela (PMT)
    private BigDecimal remainingBalance; // Saldo devedor após o pagamento da parcela

    public LoanInstallment(int installmentNumber, BigDecimal interest, BigDecimal amortization, BigDecimal installmentValue, BigDecimal remainingBalance) {
        this.installmentNumber = installmentNumber;
        this.interest = interest;
        this.amortization = amortization;
        this.installmentValue = installmentValue;
        this.remainingBalance = remainingBalance;
    }
}
