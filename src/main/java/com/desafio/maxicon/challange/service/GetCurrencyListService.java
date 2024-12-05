//package com.desafio.maxicon.currency.service;
//
//import com.desafio.maxicon.currency.model.CurrencyList;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//@Service
//public class GetCurrencyListService {
//    private ConsumoAPIService consumoAPIService = new ConsumoAPIService();
//
//    private final String ENDERECO = "https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/Moedas";
//
//
//    public List<CurrencyList> consumoApi() {
//        var json = consumoAPIService.obterDados(ENDERECO);
//
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            // Usando a referência de tipo para deserializar o JSON em uma lista dentro do campo "value"
//            var result = mapper.readTree(json);
//            var valueArray = result.path("value");  // Pegando o array de moedas
//            return mapper.readValue(valueArray.toString(), new TypeReference<List<CurrencyList>>() {});
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Erro ao converter JSON para lista de CurrencyList: " + e.getMessage());
//        }
//    }
//}