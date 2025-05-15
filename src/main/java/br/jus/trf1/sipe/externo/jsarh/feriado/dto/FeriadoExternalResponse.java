package br.jus.trf1.sipe.externo.jsarh.feriado.dto;

import br.jus.trf1.sipe.externo.jsarh.feriado.FeriadoExternal;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_SAIDA_DATA;


@Builder
@AllArgsConstructor
@Getter
public class FeriadoExternalResponse {

    @JsonFormat(pattern = PADRAO_SAIDA_DATA, shape = JsonFormat.Shape.STRING)
    private final LocalDate data;

    private final String descricao;

    private final Integer abrangencia;

    private final Integer tipo;

    public FeriadoExternal toModel() {
        return FeriadoExternal.builder()
                .data(data)
                .descricao(descricao)
                .abrangencia(abrangencia)
                .tipo(tipo)
                .build();
    }
}
