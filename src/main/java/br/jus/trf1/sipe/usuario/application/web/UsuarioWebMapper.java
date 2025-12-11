package br.jus.trf1.sipe.usuario.application.web;

import br.jus.trf1.sipe.usuario.application.web.dto.UsuarioResponse;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import br.jus.trf1.sipe.usuario.application.web.dto.UsuarioNovoRequest;

public class UsuarioWebMapper {

    public static UsuarioResponse toResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .matricula(usuario.getMatricula())
                .cracha(usuario.getCracha())
                .horaDiaria(usuario.getHoraDiaria())
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

}