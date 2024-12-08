package com.desafio.maxicon.challange.model.loan;

import com.desafio.maxicon.challange.model.Currencies;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DataGetPrice(
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date_start,
        BigDecimal pv, // Alterado para BigDecimal
        BigDecimal fees_i, // Alterado para BigDecimal
        int period_n,
        BigDecimal ptax,
        Currencies currency
) {
}
