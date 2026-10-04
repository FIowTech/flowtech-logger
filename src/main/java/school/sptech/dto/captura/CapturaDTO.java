package school.sptech.dto.captura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

// Ignora campos não declarados nesta Classe
@JsonIgnoreProperties(ignoreUnknown = true)
public record CapturaDTO(
        @JsonProperty("id_embarcado")
        Integer idEmbarcado,

        @JsonProperty("id_empresa")
        Integer idEmpresa,

        @JsonProperty("endereco_mac")
        String enderecoMac,

        String apelido,

        String modelo,

        String timestamp,

        Map<String, ComponenteDTO> componentes
) {}
