package com.desafio.maxicon.challange.controller;

import com.desafio.maxicon.challange.model.persistence.Client;
import com.desafio.maxicon.challange.model.dto.ClientDTO;
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
    public void createClient (@RequestBody @Valid ClientDTO data){
        clientRepository.save(new Client(data));
    }

    @GetMapping
    public List<Client> getClientsByName(String name) {
        return clientRepository.findAll();
    }


    @PutMapping
    @Transactional
    public void editClient(@RequestBody @Valid ClientDTO data) {
        var client = clientRepository.getReferenceById(data.id());
        client.updateClient(data);
    }

    @DeleteMapping("/{id}")
    public void removeClient(@PathVariable long id) {
        clientRepository.deleteById(id);
    }


}
