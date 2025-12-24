package br.jus.trf1.sipe.registro.infrastructure.coletor.dto;

import br.jus.trf1.sipe.registro.domain.model.Registro;
import br.jus.trf1.sipe.registro.domain.model.Sentido;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record RegistroColetorResponse(@JsonProperty("acesso") Integer acesso,
                                      @JsonProperty("data_hora") LocalDateTime dataHora,
                                      @JsonProperty("id_pedestre") Integer idPedestre,
                                      @JsonProperty("cracha") String cracha,
                                      @JsonProperty("sentido") Character sentido) {
    public Registro toModel() {
        return Registro.builder().
                codigoAcesso(acesso).
                hora(dataHora.toLocalTime()).
                sentido(Sentido.toEnum(sentido)).
                build();
    }
}
