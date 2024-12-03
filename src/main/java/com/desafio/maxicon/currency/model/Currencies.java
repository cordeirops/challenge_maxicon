package com.desafio.maxicon.currency.model;

public enum Currencies {

    AUD("AUD", "Dólar australiano"),
    CAD("CAD", "Dólar canadense"),
    CHF("CHF", "Franco suíço"),
    DKK("DKK", "Coroa dinamarquesa"),
    EUR("EUR", "Euro"),
    GBP("GBP", "Libra Esterlina"),
    JPY("JPY", "Iene"),
    NOK("NOK", "Coroa norueguesa"),
    SEK("SEK", "Coroa sueca"),
    USD("USD", "Dólar dos Estados Unidos");

    private final String simbolo;
    private final String nomeFormatado;

    Currencies(String simbolo, String nomeFormatado) {
        this.simbolo = simbolo;
        this.nomeFormatado = nomeFormatado;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public String getNomeFormatado() {
        return nomeFormatado;
    }

    // Método estático para buscar uma moeda pelo seu símbolo
    public static Currencies fromSimbolo(String simbolo) {
        for (Currencies currencie : Currencies.values()) {
            if (currencie.getSimbolo().equalsIgnoreCase(simbolo)) {
                return currencie;
            }
        }
        throw new IllegalArgumentException("Moeda com símbolo " + simbolo + " não encontrada.");
    }
}
