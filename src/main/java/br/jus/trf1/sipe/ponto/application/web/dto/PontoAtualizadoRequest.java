package br.jus.trf1.sipe.ponto.application.web.dto;

import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.ponto.domain.model.PontoId;
import br.jus.trf1.sipe.registro.application.web.dto.RegistroRequest;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

import static br.jus.trf1.sipe.comum.util.PadroesParaDataTempo.PADRAO_ENTRADA_DATA;

public record PontoAtualizadoRequest
        (@NotBlank(message = "O campo 'matricula' não pode ser branco ou nulo!")
                                     String matricula,
                                     @NotNull(message = "O campo 'dia' não pode ser nulo!")
                                     @JsonFormat(pattern = PADRAO_ENTRADA_DATA, shape = JsonFormat.Shape.STRING)
                                     LocalDate dia,
                                     @NotBlank(message = "O campo 'descricao' não pode ser branco ou nulo!")
                                     String descricao,
                                     List<RegistroRequest> registros) {
    public Ponto toDomain() {
        return Ponto.builder()
                .id(PontoId.builder()
                        .usuario(Usuario.builder()
                                .matricula(matricula)
                                .build())
                        .dia(dia)
                        .build())
                .descricao(descricao)
                .registros(registros.stream().map(RegistroRequest::toDomain).toList())
                .build();
    }
}
