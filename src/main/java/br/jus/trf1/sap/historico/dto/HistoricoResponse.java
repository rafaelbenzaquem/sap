package br.jus.trf1.sap.historico.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record HistoricoResponse(@JsonProperty("acesso")Integer acesso,
                                @JsonProperty("data_hora") LocalDateTime dataHora,
                                @JsonProperty("id_pedestre")Integer idPedestre,
                                @JsonProperty("cracha")String cracha,
                                @JsonProperty("sentido")Character sentido) {
}
