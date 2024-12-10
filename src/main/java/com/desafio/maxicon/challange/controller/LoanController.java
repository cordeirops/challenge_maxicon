package com.desafio.maxicon.challange.controller;

import com.desafio.maxicon.challange.model.currency.Currencies;
import com.desafio.maxicon.challange.model.loan.LoanType;
import com.desafio.maxicon.challange.model.dto.InstallmentDTO;
import com.desafio.maxicon.challange.model.loan.Loan;
import com.desafio.maxicon.challange.model.dto.LoanDTO;
import com.desafio.maxicon.challange.model.client.Client;
import com.desafio.maxicon.challange.repository.ClientRepository;
import com.desafio.maxicon.challange.repository.InstallmentRepository;
import com.desafio.maxicon.challange.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/loan")
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private final InstallmentRepository installmentRepository;


    private final ClientRepository clientRepository;

    public LoanController(LoanRepository installmentRepository, InstallmentRepository installmentRepository1, ClientRepository clientRepository) {
        this.installmentRepository = installmentRepository1;
        this.clientRepository = clientRepository;
    }

    @GetMapping("/types")
    public List<LoanType> getLoanType() {
        return List.of(LoanType.values());
    }


    @GetMapping("/list-loan")
    public List<Loan> listLoan() {
        return loanRepository.findAll();
    }

    @PostMapping("/save-loan")
    @Transactional
    public void saveLoan(@RequestBody @Valid LoanDTO data) {
        if (data.client_id() == null) {
            throw new IllegalArgumentException("O client_id não pode ser nulo");
        }

        Client client = clientRepository.findById(data.client_id())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));


        Loan loan = new Loan();
        loan.setClient(client);  // Associando o cliente ao LoanPrice
        loan.setClient_name(client.getName());

        LoanType loanType = LoanType.valueOf(String.valueOf(data.loanType()));
        loan.setLoanType(loanType);
        loan.setAmount_pv(data.amount_pv());
        loan.setFees_i(data.fees_i());
        loan.setPeriod_n(data.period_n());
        loan.setDate_start(data.date_start());
        loan.setPtax(data.ptax());

        Currencies currency = Currencies.valueOf(String.valueOf(data.currency()));  // Conversão de string para enum
        loan.setCurrency(currency);
        loan.setDate_end(data.date_end());
        loan.setTotal_loan(data.total_loan());
        loan.setAmount_pv(data.amount_pv());

        loan.setAjustedAmount(data.ajustedAmount());

        // Aqui você adiciona as parcelas ao LoanPrice
        if (data.installments() != null && !data.installments().isEmpty()) {
            for (InstallmentDTO installment : data.installments()) {
                installment.setLoan(loan);
            }
            loan.setInstallments(data.installments());
        }


        loanRepository.save(loan);  // O id será gerado automaticamente aqui
    }

    @PostMapping("/calculate-price")
    public Loan getPrice(@RequestBody @Valid LoanDTO loanDTO) {
        Loan loan = new Loan();


        Client client = clientRepository.findById(loanDTO.client_id())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));


        loan.setClient(client);
        loan.setLoanType(loanDTO.loanType());
        loan.setAmount_pv(loanDTO.amount_pv());
        loan.setFees_i(loanDTO.fees_i());
        loan.setPeriod_n(loanDTO.period_n());
        loan.setDate_start(loanDTO.date_start());
        loan.setPtax(loanDTO.ptax());
        loan.setCurrency(loanDTO.currency());


        loan.priceCalc();

        loan.setId(0);

        return loan;
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLoan(@PathVariable long id) {

        List<Loan> loans = loanRepository.findByClient_Id(id);

        for (Loan loan : loans) {
            installmentRepository.deleteByLoanId(id);
        }

        // Excluir os empréstimos
        loanRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }
}

