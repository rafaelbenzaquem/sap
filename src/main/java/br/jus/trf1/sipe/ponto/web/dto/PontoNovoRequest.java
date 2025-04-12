package br.jus.trf1.sipe.ponto.web.dto;

import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.ponto.PontoId;
import br.jus.trf1.sipe.registro.web.dto.RegistroNovoRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

import static br.jus.trf1.sipe.comum.util.ConstantesParaDataTempo.PADRAO_ENTRADA_DATA;

public record PontoNovoRequest(@NotBlank(message = "O campo 'matricula' não pode ser branco ou nulo!")
                           String matricula,
                               @NotNull(message = "O campo 'dia' não pode ser nulo!")
                           @JsonFormat(pattern = PADRAO_ENTRADA_DATA, shape = JsonFormat.Shape.STRING)
                           LocalDate dia,
                               @Valid
                           List<RegistroNovoRequest> registros) {
    public Ponto toModel() {
        var id = PontoId.builder()
                .matricula(this.matricula)
                .dia(this.dia)
                .build();
        return Ponto.builder()
                .id(id)
                .registros(this.registros.stream().map(RegistroNovoRequest::toModel).toList())
                .build();
    }
}
