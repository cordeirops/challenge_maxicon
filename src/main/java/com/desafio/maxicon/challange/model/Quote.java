package com.desafio.maxicon.challange.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Quote(
        @JsonAlias("paridadeCompra") String paridadeCompra,
        @JsonAlias("paridadeVenda") String paridadeVenda,
        @JsonAlias("cotacaoCompra") String cotacaoCompra,
        @JsonAlias("cotacaoVenda") String cotacaoVenda,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        @JsonAlias("dataHoraCotacao") Date dataHoraCotacao,
        @JsonAlias ("tipoBoletim") String tipoBoletim
        ) {
}
