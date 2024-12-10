package com.desafio.maxicon.challange.model.dto;

import com.desafio.maxicon.challange.model.loan.LoanType;
import com.desafio.maxicon.challange.model.currency.Currencies;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record LoanDTO(

        @Enumerated(EnumType.STRING)
        LoanType loanType,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date_start,

        BigDecimal amount_pv,

        BigDecimal fees_i,

        int period_n,

        BigDecimal ptax,

        @Enumerated(EnumType.STRING)
        Currencies currency,

        Long client_id,

        List<InstallmentDTO> installments,

        LocalDate date_end,

        BigDecimal saldoDevedor,

        BigDecimal total_loan,

        BigDecimal ajustedAmount,

        BigDecimal amortization

) {
}
