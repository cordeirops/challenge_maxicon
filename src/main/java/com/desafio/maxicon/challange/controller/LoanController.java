package com.desafio.maxicon.challange.controller;

import com.desafio.maxicon.challange.model.Currencies;
import com.desafio.maxicon.challange.model.LoanType;
import com.desafio.maxicon.challange.model.dto.ClientDTO;
import com.desafio.maxicon.challange.model.loan.LoanInstallment;
import com.desafio.maxicon.challange.model.loan.LoanPrice;
import com.desafio.maxicon.challange.model.loan.DataGetPrice;
import com.desafio.maxicon.challange.model.persistence.Client;
import com.desafio.maxicon.challange.repository.ClientRepository;
import com.desafio.maxicon.challange.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import javax.xml.crypto.Data;
import java.util.List;

@RestController
@RequestMapping("/loan")
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;

    private final ClientRepository clientRepository;  // Vamos injetar o repositório do Cliente

    // Injeção do repositório ClientRepository via construtor
    public LoanController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/types")
    public List<LoanType> getLoanType() {
        return List.of(LoanType.values());
    }

    @PostMapping("/save-loan")
    @Transactional
    public void saveLoan(@RequestBody @Valid DataGetPrice data) {
        if (data.client_id() == null) {
            throw new IllegalArgumentException("O client_id não pode ser nulo");
        }

        // Buscando o cliente
        Client client = clientRepository.findById(data.client_id())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        // Criando o LoanPrice
        LoanPrice loanPrice = new LoanPrice();
        loanPrice.setClient(client);  // Associando o cliente ao LoanPrice

        LoanType loanType = LoanType.valueOf(String.valueOf(data.loanType()));
        loanPrice.setLoanType(loanType);
        loanPrice.setAmount_pv(data.amount_pv());
        loanPrice.setFees_i(data.fees_i());
        loanPrice.setPeriod_n(data.period_n());
        loanPrice.setDate_start(data.date_start());
        loanPrice.setPtax(data.ptax());
        // Convertendo a moeda de string para a enum Currencies
        Currencies currency = Currencies.valueOf(String.valueOf(data.currency()));  // Conversão de string para enum
        loanPrice.setCurrency(currency);
        loanPrice.setDate_end(data.date_end());
        loanPrice.setTotal_loan(data.total_loan());
        loanPrice.setAmount_pv(data.amount_pv());
        //loanPrice.setAmortization(data.amortization());
        loanPrice.setAjustedAmount(data.ajustedAmount());

        // Aqui você adiciona as parcelas ao LoanPrice
        if (data.installments() != null && !data.installments().isEmpty()) {
            for (LoanInstallment installment : data.installments()) {
                installment.setLoanPrice(loanPrice);  // Associando a parcela ao LoanPrice
            }
            loanPrice.setInstallments(data.installments());  // Setando as parcelas no LoanPrice
        }

        // Salvando o LoanPrice (as parcelas serão salvas automaticamente devido ao CascadeType.ALL)
        loanRepository.save(loanPrice);  // O id será gerado automaticamente aqui
    }

    @PostMapping("/calculate-price")
    public LoanPrice getPrice(@RequestBody @Valid DataGetPrice dataGetPrice) {
        LoanPrice loanPrice = new LoanPrice();

        // Buscando o cliente pelo client_id
        Client client = clientRepository.findById(dataGetPrice.client_id())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        // Setando os dados do cliente e os outros dados da requisição
        loanPrice.setClient(client);
        loanPrice.setLoanType(dataGetPrice.loanType());
        loanPrice.setAmount_pv(dataGetPrice.amount_pv());
        loanPrice.setFees_i(dataGetPrice.fees_i());
        loanPrice.setPeriod_n(dataGetPrice.period_n());
        loanPrice.setDate_start(dataGetPrice.date_start());
        loanPrice.setPtax(dataGetPrice.ptax());
        loanPrice.setCurrency(dataGetPrice.currency());

        // Calculando o preço do empréstimo
        loanPrice.priceCalc();

        loanPrice.setId(0);  // Ou simplesmente não setar o id, dependendo da sua lógica, isso define o id como nulo.

        return loanPrice;
    }
}
