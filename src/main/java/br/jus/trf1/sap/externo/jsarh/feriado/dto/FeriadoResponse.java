package br.jus.trf1.sap.externo.jsarh.feriado.dto;

import br.jus.trf1.sap.externo.jsarh.feriado.Feriado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;


@Builder
@AllArgsConstructor
@Getter
public class FeriadoResponse {

    private final LocalDate data;

    private final String descricao;

    private final Integer abrangencia;

    private final Integer tipo;

    public Feriado toModel() {
        return Feriado.builder()
                .data(data)
                .descricao(descricao)
                .abrangencia(abrangencia)
                .tipo(tipo)
                .build();
    }
}
