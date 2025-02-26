package br.jus.trf1.sap.externo.jsarh.ausencias.licenca.dto;

import br.jus.trf1.sap.externo.jsarh.ausencias.licenca.Licenca;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record LicencaResponse(@JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING) LocalDate inicio,
                              @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)LocalDate fim,
                              String descricao, String sei) {

    public  Licenca toModel() {
        return new Licenca(inicio, fim, descricao, sei);
    }
}
