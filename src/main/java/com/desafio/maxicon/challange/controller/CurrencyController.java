package com.desafio.maxicon.challange.controller;



import com.desafio.maxicon.challange.model.Client;
import com.desafio.maxicon.challange.model.Quote;
import com.desafio.maxicon.challange.repository.ClientRepository;
import com.desafio.maxicon.challange.service.GetQuoteService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class CurrencyController {
    @Autowired
    private ClientRepository actionClient;


    @GetMapping
   public List<Quote> getQuote() {
        GetQuoteService getQuoteService = new GetQuoteService();
       return getQuoteService.currencyQuote("USD","12-04-2024");
    }
}
