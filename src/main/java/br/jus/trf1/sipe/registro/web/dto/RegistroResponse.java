package br.jus.trf1.sipe.registro.web.dto;

import br.jus.trf1.sipe.registro.Registro;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
@Relation(collectionRelation = "registros")
public record RegistroResponse(Long id,
                               @JsonFormat(pattern = "HH:mm:ss", shape = JsonFormat.Shape.STRING)
                               LocalTime hora,
                               String sentido,
                               Boolean ativo,
                               @JsonProperty("data_criacao")
                               LocalDateTime dataCriacao,
                               @JsonProperty("matricula_criador")
                               String matriculaCriador,
                               @JsonProperty("codigo_acesso")
                               Integer codigoAcesso,
                               @JsonProperty("matricula_aprovador")
                               String matriculaAprovador,
                               @JsonProperty("data_aprovacao")
                               LocalDateTime dataAprovacao
) {
    public static RegistroResponse of(Registro registro) {
        return RegistroResponse.builder()
                .id(registro.getId())
                .hora(registro.getHora())
                .sentido(registro.getSentido().getPalavra())
                .ativo(registro.getAtivo())
                .matriculaCriador(registro.getServidorCriador() == null ? null : registro.getServidorCriador().getMatricula())
                .dataCriacao(registro.getDataCadastro().toLocalDateTime())
                .codigoAcesso(registro.getCodigoAcesso())
                .matriculaAprovador(registro.getServidorAprovador() == null ? null : registro.getServidorAprovador().getMatricula())
                .dataAprovacao(registro.getDataAprovacao() == null ? null : registro.getDataAprovacao().toLocalDateTime())
                .build();
    }
}
