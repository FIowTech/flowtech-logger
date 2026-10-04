package school.sptech.dto.captura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

// Ignora campos não declarados nesta Classe
@JsonIgnoreProperties(ignoreUnknown = true)
public record ComponenteDTO(
        @JsonProperty("id_componente")
        Integer idComponente,

        Map<String, MetricaDTO> metricas
) {}
