package br.jus.trf1.sipe.usuario;

import br.jus.trf1.sipe.usuario.application.web.dto.UsuarioResponse;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import br.jus.trf1.sipe.usuario.infrastructure.persistence.UsuarioJpa;
import br.jus.trf1.sipe.usuario.application.web.dto.UsuarioNovoRequest;

public class UsuarioMapper {

    public static UsuarioResponse toResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .matricula(usuario.getMatricula())
                .cracha(usuario.getCracha())
                .horaDiaria(usuario.getHoraDiaria())
                .build();
    }

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

    public static Usuario toDomain(UsuarioNovoRequest usuarioJpa){
        return Usuario.builder()
                .nome(usuarioJpa.nome())
                .matricula(usuarioJpa.matricula())
                .cracha(usuarioJpa.cracha())
                .horaDiaria(usuarioJpa.horaDiaria())
                .build();
    }

    public static UsuarioJpa toEntity(Usuario usuario){
        return new UsuarioJpa(usuario.getId(), usuario.getNome(), usuario.getMatricula(), usuario.getCracha(), usuario.getHoraDiaria());
    }

}