package br.jus.trf1.sipe.usuario.infrastructure.db;

import br.jus.trf1.sipe.usuario.domain.model.Usuario;

public class UsuarioJpaMapper {

    public static Usuario toDomain(UsuarioJpa usuarioJpa){
        return Usuario.builder()
                .id(usuarioJpa.getId())
                .nome(usuarioJpa.getNome())
                .matricula(usuarioJpa.getMatricula())
                .cracha(usuarioJpa.getCracha())
                .horaDiaria(usuarioJpa.getHoraDiaria())
                .pontos(usuarioJpa.getPontos())
                .ausencias(usuarioJpa.getAusencias())
                .build();
    }

    public static UsuarioJpa toEntity(Usuario usuario){
        return new UsuarioJpa(usuario.getId(), usuario.getNome(), usuario.getMatricula(), usuario.getCracha(), usuario.getHoraDiaria());
    }

}
