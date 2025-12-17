package br.jus.trf1.sipe.ausencia.ausencia.infrastructure.jpa;

import br.jus.trf1.sipe.ausencia.ausencia.domain.model.Ausencia;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import br.jus.trf1.sipe.usuario.infrastructure.jpa.UsuarioJpa;

public class AusenciaJpaMapper {
    public static Ausencia toDomain(AusenciaJpa ausenciaJpa) {
        return Ausencia.builder()
                .id(ausenciaJpa.getId())
                .inicio(ausenciaJpa.getInicio())
                .fim(ausenciaJpa.getFim())
                .descricao(ausenciaJpa.getDescricao())
                .usuario(Usuario.builder()
                        .id(ausenciaJpa.getUsuario().getId())
                        .build())
                .build();
    }

    public static AusenciaJpa toEntity(Ausencia ausencia) {
        return AusenciaJpa.builder()
                .id(ausencia.getId())
                .inicio(ausencia.getInicio())
                .fim(ausencia.getFim())
                .descricao(ausencia.getDescricao())
                .usuario(UsuarioJpa.builder()
                        .id(ausencia.getUsuario().getId())
                        .build())
                .build();
    }
}
