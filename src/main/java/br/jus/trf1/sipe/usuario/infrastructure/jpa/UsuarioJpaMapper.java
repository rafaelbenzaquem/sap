package br.jus.trf1.sipe.usuario.infrastructure.jpa;

import br.jus.trf1.sipe.ausencia.ausencia.infrastructure.jpa.AusenciaJpaMapper;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;

public class UsuarioJpaMapper {

    private UsuarioJpaMapper() {
    }

    public static Usuario toDomain(UsuarioJpa usuarioJpa) {
        return Usuario.builder()
                .id(usuarioJpa.getId())
                .nome(usuarioJpa.getNome())
                .matricula(usuarioJpa.getMatricula())
                .cracha(usuarioJpa.getCracha())
                .horaDiaria(usuarioJpa.getHoraDiaria())
                .ausencias(usuarioJpa.getAusencias().stream().map(AusenciaJpaMapper::toDomain).toList())
                .build();
    }



    public static UsuarioJpa toEntity(Usuario usuario) {
        return new UsuarioJpa(usuario.getId(), usuario.getNome(), usuario.getMatricula(), usuario.getCracha(), usuario.getHoraDiaria());
    }

}
