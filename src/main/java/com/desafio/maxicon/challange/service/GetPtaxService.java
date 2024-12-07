package com.desafio.maxicon.challange.service;

import com.desafio.maxicon.challange.model.Ptax;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetPtaxService {
    private final String ENDERECO = "https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/CotacaoMoedaDia(moeda=@moeda,dataCotacao=@dataCotacao)?";
    private final String PARAMETROS = "&$top=100&$format=json";

    private ConsumoAPIService consumoAPIService = new ConsumoAPIService();

    public List<Ptax> currencyPtax(String currency, String date) {
        // Formatar a data no formato MM-dd-yyyy
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        Date formattedDate;
        try {
            formattedDate = sdf.parse(date); // Parse da data fornecida
        } catch (Exception e) {
            throw new RuntimeException("Erro ao formatar a data: " + e.getMessage());
        }

        // Gerando a string da data formatada
        String formattedDateString = sdf.format(formattedDate);

        // Construindo a URL da API com os parâmetros moeda e data
        String uri = String.format("%s@moeda='%s'&@dataCotacao='%s'%s", ENDERECO, currency, formattedDateString, PARAMETROS);

        // Obtendo os dados da API
        String json = consumoAPIService.obterDados(uri);

        System.out.println("Requisição para a API: " + uri);


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS, true);

        try {
            // Parse do JSON
            JsonNode result = mapper.readTree(json);
            JsonNode valueArray = result.path("value");  // Pegando o array "value" do JSON

            // Verificando se o array "value" existe
            if (valueArray.isArray()) {
                // Convertendo o JSON para uma lista de objetos Ptax e filtrando pelo tipoBoletim "Fechamento PTAX"
                List<Ptax> ptaxList = mapper.readValue(valueArray.toString(), new TypeReference<List<Ptax>>() {})
                        .stream()
                        .filter(ptax -> "Fechamento PTAX".equals(ptax.tipoBoletim()))  // Filtrando pelo tipo "Fechamento PTAX"
                        .collect(Collectors.toList());
                System.out.println("Resposta JSON: " + ptaxList);
                return ptaxList;  // Retorna a lista filtrada com apenas os dados de "Fechamento PTAX"
            } else {
                throw new RuntimeException("O array 'value' não foi encontrado na resposta da API.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao converter JSON para lista de Ptax: " + e.getMessage());
        }
    }
}
