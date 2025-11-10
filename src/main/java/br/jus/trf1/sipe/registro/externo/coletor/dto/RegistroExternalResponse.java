package br.jus.trf1.sipe.registro.externo.coletor.dto;

import br.jus.trf1.sipe.registro.externo.coletor.RegistroExternal;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record RegistroExternalResponse(@JsonProperty("acesso") Integer acesso,
                                       @JsonProperty("data_hora") LocalDateTime dataHora,
                                       @JsonProperty("id_pedestre") Integer idPedestre,
                                       @JsonProperty("cracha") String cracha,
                                       @JsonProperty("sentido") Character sentido) {
    public RegistroExternal toModel() {
        return RegistroExternal.builder().
                acesso(acesso).
                dataHora(dataHora).
                idPedestre(idPedestre).
                cracha(cracha).
                sentido(sentido).
                build();
    }
}
