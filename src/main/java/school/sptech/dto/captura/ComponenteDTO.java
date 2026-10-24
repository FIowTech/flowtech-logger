package school.sptech.dto.captura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ComponenteDTO(
        String nome,

        Double valor,

        String situacao,

        @JsonProperty("limite_max")
        Double limiteMax,

        @JsonProperty("limite_min")
        Double limiteMin
) {}

// Ignora campos não declarados nesta Classe
//@JsonIgnoreProperties(ignoreUnknown = true)
//public record ComponenteDTO(
//        @JsonProperty("id_componente")
//        Integer idComponente,
//
//        Map<String, MetricaDTO> metricas
//) {}
