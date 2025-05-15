package br.jus.trf1.sipe.externo.coletor.historico.dto;

import br.jus.trf1.sipe.registro.Registro;
import br.jus.trf1.sipe.registro.Sentido;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record HistoricoExternalResponse(@JsonProperty("acesso") Integer acesso,
                                        @JsonProperty("data_hora") LocalDateTime dataHora,
                                        @JsonProperty("id_pedestre") Integer idPedestre,
                                        @JsonProperty("cracha") String cracha,
                                        @JsonProperty("sentido") Character sentido) {
    public Registro toModel() {
        return Registro.builder().
                hora(dataHora.toLocalTime()).
                sentido(Sentido.toEnum(sentido).getCodigo()).
                codigoAcesso(acesso).
                build();
    }
}
