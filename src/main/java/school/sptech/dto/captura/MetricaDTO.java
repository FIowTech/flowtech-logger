package school.sptech.dto.captura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// Ignora campos não declarados nesta Classe
@JsonIgnoreProperties(ignoreUnknown = true)
public record MetricaDTO(
        @JsonProperty("ultima_leitura")
        String ultimaLeitura,

        Double valor,

        Double total,

        @JsonProperty("unidade_medida")
        String unidadeMedida,

        @JsonProperty("limite_max")
        Double limiteMax,

        @JsonProperty("limite_min")
        Double limiteMin,

        String situacao
) {}
