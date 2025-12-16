package br.jus.trf1.sipe.ausencia.licenca.infrastructure.jsarh.dto;

import br.jus.trf1.sipe.ausencia.licenca.domain.domain.Licenca;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record LicencaExternoResponse(Integer id,
                                     @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING) LocalDate inicio,
                                     @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING) LocalDate fim,
                                     String descricao, String sei) {

    public Licenca toModel() {
        return Licenca.builder()
                .id("licenca:" + id)
                .inicio(inicio)
                .fim(fim)
                .descricao(descricao)
                .sei(sei)
                .build();
    }
}
