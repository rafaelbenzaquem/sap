package br.jus.trf1.sap.externo.jsarh.feriado.dto;

import br.jus.trf1.sap.externo.jsarh.feriado.Feriado;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

import static br.jus.trf1.sap.comum.util.ConstantesDataTempoUtil.PADRAO_SAIDA_DATA;


@Builder
@AllArgsConstructor
@Getter
public class FeriadoResponse {

    @JsonFormat(pattern = PADRAO_SAIDA_DATA, shape = JsonFormat.Shape.STRING)
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
