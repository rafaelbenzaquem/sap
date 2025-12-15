package br.jus.trf1.sipe.feriado.infrastructure.jsarh.dto;

import br.jus.trf1.sipe.feriado.domain.model.Feriado;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_SAIDA_DATA;


@Builder
public record FeriadoJSarhResponse(
        @JsonFormat(pattern = PADRAO_SAIDA_DATA, shape = JsonFormat.Shape.STRING) LocalDate data, String descricao,
        Integer abrangencia, Integer tipo) {

    public Feriado toModel() {
        return new Feriado(data, descricao, abrangencia, tipo);
    }
}
