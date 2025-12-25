package br.jus.trf1.sipe.usuario.infrastructure.jpa;

import br.jus.trf1.sipe.ausencia.ausencia.infrastructure.jpa.AusenciaJpa;
import br.jus.trf1.sipe.ausencia.ausencia.infrastructure.jpa.AusenciaJpaMapper;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;

import java.util.List;

public class UsuarioJpaMapper {

    private UsuarioJpaMapper() {
    }

    public static Usuario toDomain(UsuarioJpa usuarioJpa, List<AusenciaJpa> ausencias) {
        return Usuario.builder()
                .id(usuarioJpa.getId())
                .nome(usuarioJpa.getNome())
                .matricula(usuarioJpa.getMatricula())
                .cracha(usuarioJpa.getCracha())
                .horaDiaria(usuarioJpa.getHoraDiaria())
                .ausencias(ausencias.stream().map(AusenciaJpaMapper::toDomain).toList())
                .build();
    }

    public static Usuario toDomain(UsuarioJpa usuarioJpa) {
        return Usuario.builder()
                .id(usuarioJpa.getId())
                .nome(usuarioJpa.getNome())
                .matricula(usuarioJpa.getMatricula())
                .cracha(usuarioJpa.getCracha())
                .horaDiaria(usuarioJpa.getHoraDiaria())
                .build();
    }


    public static UsuarioJpa toEntity(Usuario usuario) {
        return new UsuarioJpa(usuario.getId(), usuario.getNome(), usuario.getMatricula(), usuario.getCracha(), usuario.getHoraDiaria());
    }

}
