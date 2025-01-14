package br.jus.trf1.sap.historico.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record HistoricoResponse(@JsonProperty("acesso")Integer acesso,
                                @JsonProperty("dataHora") LocalDateTime dataHora,
                                @JsonProperty("idPedestre")Integer idPedestre,
                                @JsonProperty("cracha")String cracha,
                                @JsonProperty("sentido")Character sentido) {
}
