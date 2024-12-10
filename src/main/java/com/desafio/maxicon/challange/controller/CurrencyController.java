package com.desafio.maxicon.challange.controller;



import com.desafio.maxicon.challange.model.dto.PtaxDTO;
import com.desafio.maxicon.challange.model.currency.Currencies;
import com.desafio.maxicon.challange.model.dto.CurrencyDTO;
import com.desafio.maxicon.challange.model.ptax.Ptax;
import com.desafio.maxicon.challange.repository.ClientRepository;
import com.desafio.maxicon.challange.service.GetPtaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currency")
public class CurrencyController {
    @Autowired
    private ClientRepository actionClient;

    @Autowired
    private GetPtaxService getPtaxService;

    @GetMapping("/get-currencies")
    public List<CurrencyDTO> getCurrencies() {
        return List.of(Currencies.values()).stream()
                .map(currency -> new CurrencyDTO(currency.name(), currency.getNomeFormatado()))
                .toList();
    }

    @PostMapping("/get-ptax")
    public ResponseEntity<List<PtaxDTO>> getPtax(@RequestBody Ptax ptax) {
        if (ptax == null || ptax.getCurrency() == null || ptax.getDate() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(List.of());
        }

        try {
            // Chama o serviço para obter os dados filtrados
            List<PtaxDTO> ptaxDTOList = getPtaxService.currencyPtax(ptax.getCurrency(), ptax.getDate());

            if (ptaxDTOList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
            }

            // Retorna a resposta com os dados filtrados
            return ResponseEntity.ok(ptaxDTOList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }
}
