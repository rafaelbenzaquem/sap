package br.jus.trf1.sipe.externo.jsarh.ausencias.licenca.dto;

import br.jus.trf1.sipe.externo.jsarh.ausencias.licenca.LicencaExternal;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record LicencaExternalResponse(@JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING) LocalDate inicio,
                                      @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)LocalDate fim,
                                      String descricao, String sei) {

    public LicencaExternal toModel() {
        return new LicencaExternal(inicio, fim, descricao, sei);
    }
}
