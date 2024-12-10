package com.desafio.maxicon.challange.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PtaxDTO(

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        @JsonAlias("dataHoraCotacao") Date dataHoraCotacao,
        @JsonAlias("tipoBoletim") String tipoBoletim,
        @JsonAlias("cotacaoVenda") BigDecimal cotacaoVenda
) {
}
