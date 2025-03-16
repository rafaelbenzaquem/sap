package br.jus.trf1.sap.registro.web;

import br.jus.trf1.sap.ponto.Ponto;
import br.jus.trf1.sap.registro.Registro;
import br.jus.trf1.sap.registro.Sentido;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

import static br.jus.trf1.sap.comum.util.ConstantesDataTempoUtil.PADRAO_ENTRADA_TEMPO;

public record RegistroNovoRequest(@JsonFormat(pattern = PADRAO_ENTRADA_TEMPO, shape = JsonFormat.Shape.STRING)
                                  @NotNull(message = "O campo 'hora' não pode ser nulo!")
                                  LocalTime hora,
                                  @NotBlank(message = "O campo 'sentido' não pode ser branco ou nulo!")
                                  String sentido,
                                  @JsonProperty(value = "codigo_acesso")
                                  Integer codigoAcesso) {
    public Registro toModel(Ponto ponto) {
        return Registro.builder()
                .hora(hora)
                .sentido(Sentido.toEnum(sentido).getCodigo())
                .codigoAcesso(codigoAcesso)
                .versao(codigoAcesso == null ? null : 1)
                .ponto(ponto)
                .build();
    }
}
