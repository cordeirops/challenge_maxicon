package com.desafio.maxicon.challange.controller;

import com.desafio.maxicon.challange.model.loan.Loan;
import com.desafio.maxicon.challange.model.client.Client;
import com.desafio.maxicon.challange.model.dto.ClientDTO;
import com.desafio.maxicon.challange.repository.ClientRepository;
import com.desafio.maxicon.challange.repository.InstallmentRepository;
import com.desafio.maxicon.challange.repository.LoanRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private InstallmentRepository installmentRepository;
    @Autowired
    private LoanRepository loanRepository;

    @PostMapping
    @Transactional
    public void createClient (@RequestBody @Valid ClientDTO data){
        clientRepository.save(new Client(data));
    }

    @GetMapping
    public List<Client> getClientsByName(String name) {
        return clientRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable long id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    @Transactional
    public void editClient(@RequestBody @Valid ClientDTO data) {
        var client = clientRepository.getReferenceById(data.id());
        client.updateClient(data);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable long id) {
        // Obter o cliente para excluir as parcelas
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (!clientOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }

        Client client = clientOptional.get();

        List<Loan> loans = loanRepository.findByClient_Id(id);
        for (Loan loan : loans) {
            installmentRepository.deleteAll(loan.getInstallments());
        }

        loanRepository.deleteAll(loans);

        clientRepository.delete(client);

        return ResponseEntity.ok().build();
    }

}
