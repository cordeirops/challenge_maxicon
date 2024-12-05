package com.desafio.maxicon.challange.controller;

import com.desafio.maxicon.challange.model.Client;
import com.desafio.maxicon.challange.model.DataCreateClient;
import com.desafio.maxicon.challange.repository.ClientRepository;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping
    @Transactional
    public void createClient (@RequestBody @Valid DataCreateClient data){
        clientRepository.save(new Client(data));
    }

    @GetMapping
    public List<Client> getClientsByName(String name) {
        return clientRepository.findAll();
    }
}
