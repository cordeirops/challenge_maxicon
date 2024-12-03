package com.desafio.maxicon.currency.service;

import com.desafio.maxicon.currency.model.Quote;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetQuoteService {
    private final String ENDERECO ="https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/CotacaoMoedaDia(moeda=@moeda,dataCotacao=@dataCotacao)?";
    private String currency;

    private String date;

    private final String PARAMETROS = "&$top=100&$format=json";

    private ConsumoAPIService consumoAPIService = new ConsumoAPIService();

    //@moeda='USD'&@dataCotacao='12-02-2024';


    public List<Quote> currencyQuote(String currency, String date) {
        var uri = ENDERECO + "@moeda=" + "'" +currency + "'" + "&@dataCotacao=" + "'" + date + "'" + PARAMETROS;

        var json = consumoAPIService.obterDados(uri);

        System.out.println(uri);

        System.out.println(json);

        ObjectMapper mapper = new ObjectMapper();
        try {
            // Usando a referência de tipo para deserializar o JSON em uma lista dentro do campo "value"
            var result = mapper.readTree(json);
            var valueArray = result.path("value");  // Pegando o array de moedas
            return mapper.readValue(valueArray.toString(), new TypeReference<List<Quote>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao converter JSON para lista de CurrencyList: " + e.getMessage());
        }
    }
}
