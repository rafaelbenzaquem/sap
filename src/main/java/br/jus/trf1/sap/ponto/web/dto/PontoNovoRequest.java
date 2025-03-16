package br.jus.trf1.sap.ponto.web.dto;

import br.jus.trf1.sap.ponto.Ponto;
import br.jus.trf1.sap.ponto.PontoId;
import br.jus.trf1.sap.registro.web.RegistroNovoRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record PontoNovoRequest(@NotBlank(message = "O campo 'matricula' não pode ser branco ou nulo!")
                               String matricula,
                               @NotNull(message = "O campo 'dia' não pode ser nulo!")
                               LocalDate dia,
                               List<RegistroNovoRequest> registros) {
    public Ponto toModel() {
        var id = PontoId.builder()
                .matricula(this.matricula)
                .dia(this.dia)
                .build();
        return Ponto.builder()
                .id(id)
                .registros(this.registros.stream().map(r -> r.toModel(Ponto.builder()
                        .id(id)
                        .build())).toList())
                .build();
    }
}
