package com.desafio.maxicon.challange.model.loan;

import com.desafio.maxicon.challange.model.Currencies;
import com.desafio.maxicon.challange.model.client.Client;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "loans")
@Getter
@Setter
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;
    private Date date_start;
    private Date date_end;
    private Currencies currencie;
    private Float fees;
    private Float ptax;
    private Float total_loan;
}
