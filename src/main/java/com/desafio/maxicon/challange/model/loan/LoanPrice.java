package com.desafio.maxicon.challange.model.loan;

import com.desafio.maxicon.challange.model.Currencies;
import com.desafio.maxicon.challange.model.persistence.Client;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loans")
@Getter
@Setter
public class LoanPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    private LocalDate date_start; // Data de início do empréstimo
    private LocalDate date_end;   // Data final do empréstimo
    private int period_n; // Número de parcelas
    private BigDecimal amount_pv; // Valor do financiamento (PV)
    private BigDecimal fees_i; // Taxa de juros (i) como BigDecimal
    private Currencies currencie;
    private BigDecimal ptax; // Taxa adicional
    private BigDecimal pmt; // Valor da parcela (PMT)
    private BigDecimal total_loan; // Valor total do empréstimo

    @Transient
    private List<LoanInstallment> installments = new ArrayList<>(); // Lista de parcelas detalhadas

    // Método para calcular a data final com base na data de início e no número de parcelas
    public void calculateEndDate() {
        if (date_start == null || period_n <= 0) {
            throw new IllegalArgumentException("Data inicial ou número de parcelas inválido");
        }

        // Adiciona o número de meses (parcelas) à data de início
        this.date_end = date_start.plusMonths(period_n); // Calcula a data final
    }

    // Método de cálculo das parcelas e do valor total do empréstimo
    public void priceCalc() {
        // Verificação de entrada
        if (fees_i == null || period_n <= 0 || amount_pv == null || amount_pv.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Dados insuficientes para cálculo do financiamento. O valor do financiamento deve ser maior que zero.");
        }

        // Validação adicional para garantir que as taxas sejam positivas
        if (fees_i.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Taxa de juros deve ser maior que zero.");
        }

        // Debugging: Exibindo valores iniciais
        System.out.println("Cálculo do Empréstimo - Valores iniciais:");
        System.out.println("Valor do financiamento: " + amount_pv);
        System.out.println("Taxa de juros: " + fees_i);
        System.out.println("Número de parcelas: " + period_n);

        // Convertendo a taxa de juros de percentual para decimal (ex: 2% = 0.02)
        BigDecimal i = fees_i.divide(BigDecimal.valueOf(100)); // Convertendo de porcentagem para decimal

        BigDecimal onePlusI = BigDecimal.ONE.add(i); // 1 + i
        BigDecimal numerator = i.multiply(onePlusI.pow(period_n)); // i * (1 + i)^n
        BigDecimal denominator = onePlusI.pow(period_n).subtract(BigDecimal.ONE); // (1 + i)^n - 1

        // Evitar erro de divisão por zero (caso o denominador seja zero)
        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Erro ao calcular o denominador: valor inválido.");
        }

        // Calculando o valor da parcela PMT
        this.pmt = amount_pv.multiply(numerator).divide(denominator, 2, RoundingMode.HALF_UP); // PMT com arredondamento

        // Debugging: Exibindo valor da parcela calculada
        System.out.println("Valor da parcela (PMT): " + pmt);

        // Calculando as parcelas detalhadas
        BigDecimal saldoDevedor = amount_pv;
        for (int k = 1; k <= period_n; k++) {
            BigDecimal interest = saldoDevedor.multiply(i); // Juros da parcela
            BigDecimal amortization = pmt.subtract(interest); // Amortização da parcela
            saldoDevedor = saldoDevedor.subtract(amortization); // Atualiza saldo devedor

            // Adiciona os detalhes da parcela à lista
            installments.add(new LoanInstallment(k, interest, amortization, pmt, saldoDevedor));

            // Debugging: Exibindo detalhes de cada parcela
            System.out.println("Parcela " + k + " - Juros: " + interest + ", Amortização: " + amortization + ", Saldo devedor: " + saldoDevedor);
        }

        // Calcula o valor total do empréstimo (soma de todas as parcelas)
        this.total_loan = pmt.multiply(BigDecimal.valueOf(period_n));

        // Debugging: Exibindo valor total do empréstimo
        System.out.println("Valor total do empréstimo: " + total_loan);

        // Calcula a data final após calcular o preço
        calculateEndDate();
    }


}
