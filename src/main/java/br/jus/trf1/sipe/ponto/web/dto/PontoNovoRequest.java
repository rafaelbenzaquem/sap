package br.jus.trf1.sipe.ponto.web.dto;

import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.ponto.PontoId;
import br.jus.trf1.sipe.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_DATA;

public record PontoNovoRequest(@NotBlank(message = "O campo 'matricula' não pode ser branco ou nulo!")
                               String matricula,
                               @NotNull(message = "O campo 'dia' não pode ser nulo!")
                               @JsonFormat(pattern = PADRAO_ENTRADA_DATA, shape = JsonFormat.Shape.STRING)
                               LocalDate dia,
                               String descricao) {
    public Ponto toModel() {
        var id = PontoId.builder()
                .usuario(Usuario.builder()
                        .matricula(this.matricula)
                        .build())
                .dia(this.dia)
                .build();
        return Ponto.builder()
                .id(id)
                .descricao(this.descricao)
                .build();
    }
}
