package com.desafio.maxicon.challange.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class PtaxDTO {
    private String currency;
    private String date;

    // Constructor para aceitar diretamente a data e corrigir se necessário
    public PtaxDTO(String currency, String date) {
        this.currency = currency;
        // Corrige a data ao ser passada para o DTO
        this.date = corrigirDataFinalDeSemana(date);
    }

    // Método para corrigir a data caso seja final de semana
    private String corrigirDataFinalDeSemana(String data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate dataLocal = LocalDate.parse(data, formatter);  // Converter a data de String para LocalDate

        // Verifica se é sábado ou domingo
        if (dataLocal.getDayOfWeek() == DayOfWeek.SATURDAY) {
            // Se for sábado, subtrai 1 dia para obter a sexta-feira
            dataLocal = dataLocal.minusDays(1);
        } else if (dataLocal.getDayOfWeek() == DayOfWeek.SUNDAY) {
            // Se for domingo, subtrai 2 dias para obter a sexta-feira
            dataLocal = dataLocal.minusDays(2);
        }
        return dataLocal.format(formatter);
    }
}
