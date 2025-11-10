package br.jus.trf1.sipe.externo.jsarh.feriado.dto;

import br.jus.trf1.sipe.externo.jsarh.feriado.FeriadoExternal;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_SAIDA_DATA;


@Builder
public record FeriadoExternalResponse(
        @JsonFormat(pattern = PADRAO_SAIDA_DATA, shape = JsonFormat.Shape.STRING) LocalDate data, String descricao,
        Integer abrangencia, Integer tipo) {

    public FeriadoExternal toModel() {
        return FeriadoExternal.builder()
                .data(data)
                .descricao(descricao)
                .abrangencia(abrangencia)
                .tipo(tipo)
                .build();
    }
}
