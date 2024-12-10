package com.desafio.maxicon.challange.model.loan;

import com.desafio.maxicon.challange.model.currency.Currencies;
import com.desafio.maxicon.challange.model.dto.InstallmentDTO;
import com.desafio.maxicon.challange.model.dto.LoanDTO;
import com.desafio.maxicon.challange.model.client.Client;
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
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @JsonProperty("client")
    private Client client;

    @Column(name = "client_name", nullable = false)
    private String client_name;

    @Enumerated(EnumType.STRING)
    private LoanType loanType;
    private LocalDate date_start;
    private LocalDate date_end;
    private int period_n;
    private BigDecimal amount_pv;
    private BigDecimal ajustedAmount;
    private BigDecimal fees_i;

    @Enumerated(EnumType.STRING)
    private Currencies currency;
    private BigDecimal ptax;
    private BigDecimal total_loan;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<InstallmentDTO> installments = new ArrayList<>();



    public Loan(@Valid LoanDTO data) {
    }

    @JsonProperty("client_id")
    public long getClientId() {
        return client != null ? client.getId() : 0;
    }

    public void calculateEndDate() {
        if (date_start == null || period_n <= 0) {
            throw new IllegalArgumentException("Data inicial ou número de parcelas inválido");
        }

        this.date_end = date_start.plusMonths(period_n);
    }

    public void priceCalc() {
        if (fees_i == null || period_n <= 0 || amount_pv == null || amount_pv.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Dados insuficientes para cálculo do financiamento. O valor do financiamento deve ser maior que zero.");
        }

        if (fees_i.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Taxa de juros deve ser maior que zero.");
        }


        this.ajustedAmount = amount_pv.multiply(ptax);

        fees_i = fees_i.divide(BigDecimal.valueOf(100));


        if (loanType == LoanType.PRICE){

        BigDecimal onePlusI = BigDecimal.ONE.add(fees_i); // 1 + fees_i
        BigDecimal numerator = fees_i.multiply(onePlusI.pow(period_n)); // fees_i * (1 + fees_i)^n
        BigDecimal denominator = onePlusI.pow(period_n).subtract(BigDecimal.ONE); // (1 + fees_i)^n - 1

        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Erro ao calcular o denominador: valor inválido.");
        }

        var pmt = ajustedAmount.multiply(numerator).divide(denominator, 2, RoundingMode.HALF_UP); // PMT com arredondamento


        var saldoDevedor = ajustedAmount;
        for (int k = 1; k <= period_n; k++) {
            var interest = saldoDevedor.multiply(fees_i);
            var amortization = pmt.subtract(interest);
            saldoDevedor = saldoDevedor.subtract(amortization);

            InstallmentDTO installment = new InstallmentDTO(null, this, k, interest, amortization, pmt, saldoDevedor);
            installments.add(installment);

        }

        this.total_loan = pmt.multiply(BigDecimal.valueOf(period_n));


        calculateEndDate();
    } else if (loanType == LoanType.SAC){
           var amortization = this.ajustedAmount.divide(BigDecimal.valueOf(this.period_n), 2, RoundingMode.HALF_UP);

           var saldoDevedor = this.ajustedAmount;


            for (int k = 1; k <= period_n; k++) {
                var interest = saldoDevedor.multiply(fees_i);

                var pmt = amortization.add(interest);

                saldoDevedor = saldoDevedor.subtract(amortization);

                InstallmentDTO installment = new InstallmentDTO(null, this, k, interest, amortization, pmt, saldoDevedor);
                installments.add(installment);

            }

            this.total_loan = installments.stream().map(InstallmentDTO::getInstallmentValue).reduce(BigDecimal.ZERO, BigDecimal::add);

            calculateEndDate();

    }
        }

}

