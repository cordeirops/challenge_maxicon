package com.desafio.maxicon.challange.model.ptax;

import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class Ptax {
    private String currency;
    private String date;

    public Ptax(String currency, String date) {
        this.currency = currency;

        this.date = corrigirDataFinalDeSemana(date);
    }

    private String corrigirDataFinalDeSemana(String data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate dataLocal = LocalDate.parse(data, formatter);

        if (dataLocal.getDayOfWeek() == DayOfWeek.SATURDAY) {
            dataLocal = dataLocal.minusDays(1);
        } else if (dataLocal.getDayOfWeek() == DayOfWeek.SUNDAY) {
            dataLocal = dataLocal.minusDays(2);
        }
        return dataLocal.format(formatter);
    }
}
