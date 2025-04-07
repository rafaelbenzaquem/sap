package br.jus.trf1.sap.ponto.web.dto;

import br.jus.trf1.sap.ponto.Ponto;
import br.jus.trf1.sap.ponto.PontoId;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

import static br.jus.trf1.sap.comum.util.ConstantesDataTempoUtil.PADRAO_ENTRADA_DATA;

public record PontoAtualizadoRequest(@NotBlank(message = "O campo 'matricula' não pode ser branco ou nulo!")
                                     String matricula,
                                     @NotNull(message = "O campo 'dia' não pode ser nulo!")
                                     @JsonFormat(pattern = PADRAO_ENTRADA_DATA, shape = JsonFormat.Shape.STRING)
                                     LocalDate dia,
                                     @NotNull(message = "O campo 'indice' não pode ser nulo!")
                                     Float indice,
                                     @NotBlank(message = "O campo 'descricao' não pode ser branco ou nulo!")
                                     String descricao
) {
    public Ponto toModel() {
        var id = PontoId.builder()
                .matricula(this.matricula)
                .dia(this.dia)
                .build();
        return Ponto.builder()
                .id(id)
                .indice(this.indice)
                .descricao(this.descricao)
                .build();
    }
}
