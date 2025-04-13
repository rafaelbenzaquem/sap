package br.jus.trf1.sipe.externo.coletor.historico.dto;

import br.jus.trf1.sipe.registro.Registro;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record HistoricoResponse(@JsonProperty("acesso") Integer acesso,
                                @JsonProperty("data_hora") LocalDateTime dataHora,
                                @JsonProperty("id_pedestre") Integer idPedestre,
                                @JsonProperty("cracha") String cracha,
                                @JsonProperty("sentido") Character sentido) {
    public Registro toModel() {
        return Registro.builder().
                hora(dataHora.toLocalTime()).
                sentido(sentido).
                codigoAcesso(acesso).
                versao(1).
                build();
    }
}
