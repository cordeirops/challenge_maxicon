package com.desafio.maxicon.challange.model.loan;

import com.desafio.maxicon.challange.model.LoanType;
import com.desafio.maxicon.challange.model.Currencies;
import com.desafio.maxicon.challange.model.persistence.Client;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public record DataGetPrice(



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


        @ManyToOne
        @JoinColumn(name = "client_id", nullable = false)
        Long client_id,

        @ManyToOne
        @JoinColumn(name = "client_name", nullable = false)
        String client_name,

        List<LoanInstallment> installments,

        LocalDate date_end,
        BigDecimal saloDevedor,
        BigDecimal total_loan,
        BigDecimal ajustedAmount,
        BigDecimal amortization
) {
}
