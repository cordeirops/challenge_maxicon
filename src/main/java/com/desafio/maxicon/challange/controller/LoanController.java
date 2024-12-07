package com.desafio.maxicon.challange.controller;

import com.desafio.maxicon.challange.model.loan.LoanPrice;
import com.desafio.maxicon.challange.model.loan.DataGetPrice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/")
public class LoanController {

    @PostMapping("/calculate-price")
    public LoanPrice getPrice(@RequestBody @Valid DataGetPrice dataGetPrice) {
        LoanPrice loanPrice = new LoanPrice();
        loanPrice.setAmount_pv(dataGetPrice.pv());
        loanPrice.setFees_i(dataGetPrice.fees_i());
        loanPrice.setPeriod_n(dataGetPrice.period_n());
        loanPrice.setDate_start(dataGetPrice.date_start());
        loanPrice.priceCalc();
        return loanPrice;
    }
}
