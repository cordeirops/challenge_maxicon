package com.desafio.maxicon.challange.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

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
