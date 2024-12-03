package com.desafio.maxicon.currency.controller;



import com.desafio.maxicon.currency.model.Quote;
import com.desafio.maxicon.currency.service.GetQuoteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/quote")
public class CurrencyController {
    @GetMapping
   public List<Quote> getQuote() {
        GetQuoteService getQuoteService = new GetQuoteService();
       return getQuoteService.currencyQuote("USD","12-03-2024");
    }
}
