package com.desafio.maxicon.challange.service;

import com.desafio.maxicon.challange.model.dto.PtaxDTO;
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

    public List<PtaxDTO> currencyPtax(String currency, String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        Date formattedDate;
        try {
            formattedDate = sdf.parse(date);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao formatar a data: " + e.getMessage());
        }

        String formattedDateString = sdf.format(formattedDate);

        String uri = String.format("%s@moeda='%s'&@dataCotacao='%s'%s", ENDERECO, currency, formattedDateString, PARAMETROS);

        String json = consumoAPIService.obterDados(uri);

        System.out.println("Requisição para a API: " + uri);


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS, true);

        try {
            JsonNode result = mapper.readTree(json);
            JsonNode valueArray = result.path("value");

            if (valueArray.isArray()) {
                List<PtaxDTO> ptaxDTOList = mapper.readValue(valueArray.toString(), new TypeReference<List<PtaxDTO>>() {})
                        .stream()
                        .filter(ptaxDTO -> "Fechamento PTAX".equals(ptaxDTO.tipoBoletim()))
                        .collect(Collectors.toList());
                System.out.println("Resposta JSON: " + ptaxDTOList);
                return ptaxDTOList;
            } else {
                throw new RuntimeException("O array 'value' não foi encontrado na resposta da API.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao converter JSON para lista de Ptax: " + e.getMessage());
        }
    }
}
