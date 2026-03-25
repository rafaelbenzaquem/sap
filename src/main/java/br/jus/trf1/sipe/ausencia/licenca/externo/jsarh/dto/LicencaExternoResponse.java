package br.jus.trf1.sipe.ausencia.licenca.externo.jsarh.dto;

import br.jus.trf1.sipe.ausencia.licenca.externo.jsarh.LicencaExterna;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record LicencaExternoResponse(String id,
                                     @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING) LocalDate inicio,
                                     @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING) LocalDate fim,
                                     String descricao, String sei) {

    public LicencaExterna toModel(String matricula) {
        return new LicencaExterna("licenca:%s:%s".formatted(matricula,id), inicio, fim, descricao, sei);
    }
}
