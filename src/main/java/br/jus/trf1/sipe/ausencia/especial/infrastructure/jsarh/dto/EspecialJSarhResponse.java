package br.jus.trf1.sipe.ausencia.especial.infrastructure.jsarh.dto;

import br.jus.trf1.sipe.ausencia.especial.domain.model.Especial;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record EspecialJSarhResponse(
        Integer id,
        @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING) LocalDate inicio,
        @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING) LocalDate fim,
        String descricao, String sei) {

    public Especial toModel() {
        return Especial.builder()
                .id("especial:" + id)
                .inicio(inicio)
                .fim(fim)
                .descricao(descricao)
                .sei(sei)
                .build();
    }
}
