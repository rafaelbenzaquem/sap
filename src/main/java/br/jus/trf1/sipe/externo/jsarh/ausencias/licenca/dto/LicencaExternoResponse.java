package br.jus.trf1.sipe.externo.jsarh.ausencias.licenca.dto;

import br.jus.trf1.sipe.externo.jsarh.ausencias.licenca.LicencaExterna;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record LicencaExternoResponse(Integer id,
                                     @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING) LocalDate inicio,
                                     @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING) LocalDate fim,
                                     String descricao, String sei) {

    public LicencaExterna toModel() {
        return new LicencaExterna("licenca:" + id, inicio, fim, descricao, sei);
    }
}
