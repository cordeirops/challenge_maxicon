package com.desafio.maxicon.challange.model.loan;

import com.desafio.maxicon.challange.model.LoanType;
import com.desafio.maxicon.challange.model.Currencies;
import com.desafio.maxicon.challange.model.persistence.Client;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
public class LoanPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @JsonProperty("client_id")
    private Client client_id;

    @ManyToOne
    @JoinColumn(name = "client_name", nullable = false)
    @JsonProperty("client_name")
    private Client client_name;

    @Enumerated(EnumType.STRING)
    private LoanType loanType;
    private LocalDate date_start; // Data de início do empréstimo
    private LocalDate date_end;   // Data final do empréstimo
    private int period_n; // Número de parcelas
    private BigDecimal amount_pv; // Valor do financiamento (PV)
    private BigDecimal ajustedAmount;
    private BigDecimal fees_i; // Taxa de juros (i) como BigDecimal

    @Enumerated(EnumType.STRING)
    private Currencies currency;
    private BigDecimal ptax; // Taxa adicional
//    private BigDecimal pmt; // Valor da parcela (PMT)
    private BigDecimal total_loan; // Valor total do empréstimo
//    private BigDecimal saldoDevedor;
//    private BigDecimal interest;
//    private BigDecimal amortization;

    // Relacionamento OneToMany
    @OneToMany(mappedBy = "loanPrice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference  // Garante a serialização correta e evita ciclos
    private List<LoanInstallment> installments = new ArrayList<>();

    public LoanPrice(@Valid DataGetPrice data) {
    }

    @JsonProperty("client_id")
    public long getClientId() {
        return client_id != null ? client_id.getId() : 0;
    }

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


        this.ajustedAmount = amount_pv.multiply(ptax);

        fees_i = fees_i.divide(BigDecimal.valueOf(100)); // Convertendo de porcentagem para decimal


        // Debugging: Exibindo valores iniciais
        System.out.println("Cálculo do Empréstimo - Valores iniciais:");
        System.out.println("Valor do financiamento: " + amount_pv);
        System.out.println("Taxa de juros: " + fees_i);
        System.out.println("Número de parcelas: " + period_n);
        System.out.println("PTAX: " + ptax);
        System.out.println("TYPE: " + loanType);
        System.out.println("Client ID: " + client_id.getId());


        if (loanType == LoanType.PRICE){

        BigDecimal onePlusI = BigDecimal.ONE.add(fees_i); // 1 + fees_i
        BigDecimal numerator = fees_i.multiply(onePlusI.pow(period_n)); // fees_i * (1 + fees_i)^n
        BigDecimal denominator = onePlusI.pow(period_n).subtract(BigDecimal.ONE); // (1 + fees_i)^n - 1

        // Evitar erro de divisão por zero (caso o denominador seja zero)
        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Erro ao calcular o denominador: valor inválido.");
        }

        // Calculando o valor da parcela PMT
        var pmt = ajustedAmount.multiply(numerator).divide(denominator, 2, RoundingMode.HALF_UP); // PMT com arredondamento

        // Debugging: Exibindo valor da parcela calculada
        System.out.println("Valor da parcela (PMT): " + pmt);

        // Calculando as parcelas detalhadas
        var saldoDevedor = ajustedAmount;
        for (int k = 1; k <= period_n; k++) {
            var interest = saldoDevedor.multiply(fees_i); // Juros da parcela
            var amortization = pmt.subtract(interest); // Amortização da parcela
            saldoDevedor = saldoDevedor.subtract(amortization); // Atualiza saldo devedor

            // Adiciona os detalhes da parcela à lista
            LoanInstallment installment = new LoanInstallment(null, this, k, interest, amortization, pmt, saldoDevedor);
            installments.add(installment);  // Adiciona a parcela à lista de parcelas


            // Debugging: Exibindo detalhes de cada parcela
            System.out.println("Parcela " + k + " - Juros: " + interest + ", Amortização: " + amortization + ", Saldo devedor: " + saldoDevedor);
        }

        // Calcula o valor total do empréstimo (soma de todas as parcelas)
        this.total_loan = pmt.multiply(BigDecimal.valueOf(period_n));

        // Debugging: Exibindo valor total do empréstimo
        System.out.println("Valor total do empréstimo: " + total_loan);

        // Calcula a data final após calcular o preço
        calculateEndDate();
    } else if (loanType == LoanType.SAC){ //Calcula o método SAC
           var amortization = this.ajustedAmount.divide(BigDecimal.valueOf(this.period_n), 2, RoundingMode.HALF_UP);

           var saldoDevedor = this.ajustedAmount;


            for (int k = 1; k <= period_n; k++) {
                var interest = saldoDevedor.multiply(fees_i);

                var pmt = amortization.add(interest);

                saldoDevedor = saldoDevedor.subtract(amortization);

                LoanInstallment installment = new LoanInstallment(null, this, k, interest, amortization, pmt, saldoDevedor);
                installments.add(installment);  // Adiciona a parcela à lista de parcelas


                // Debugging: Exibindo detalhes de cada parcela
                System.out.println("Parcela " + k + " - Juros: " + interest + ", Amortização: " + amortization + ", PMT: " + pmt + ", Saldo Devedor: " + saldoDevedor);
            }

            // Calcula o valor total do empréstimo (soma de todas as parcelas)
            this.total_loan = installments.stream().map(LoanInstallment::getInstallmentValue).reduce(BigDecimal.ZERO, BigDecimal::add);

            // Debugging: Exibindo valor total do empréstimo
            System.out.println("Valor total do empréstimo (SAC): " + total_loan);


            // Calcula a data final após calcular o preço
            calculateEndDate();


    }
        }

}

