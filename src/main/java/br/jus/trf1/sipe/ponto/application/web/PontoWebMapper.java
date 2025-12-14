package br.jus.trf1.sipe.ponto.application.web;

import br.jus.trf1.sipe.ponto.application.web.dto.PontoAtualizadoResponse;
import br.jus.trf1.sipe.ponto.application.web.dto.PontoNovoRequest;
import br.jus.trf1.sipe.ponto.application.web.dto.PontoNovoResponse;
import br.jus.trf1.sipe.ponto.application.web.dto.PontoResponse;
import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.ponto.domain.model.PontoId;
import br.jus.trf1.sipe.ponto.infrastructure.jpa.PontoJpa;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;

public class PontoWebMapper {


    private PontoWebMapper() {
    }

    public static Ponto toDomain(PontoNovoRequest pontoNovoRequest) {
        var id = PontoId.builder()
                .usuario(Usuario.builder()
                        .matricula(pontoNovoRequest.matricula())
                        .build())
                .dia(pontoNovoRequest.dia())
                .build();
        return Ponto.builder()
                .id(id)
                .descricao(pontoNovoRequest.descricao())
                .build();
    }

    public static PontoResponse toResponse(Ponto ponto) {
        return new PontoResponse(ponto.getId().getUsuario().getMatricula(), ponto.getId().getDia(),
                ponto.getDescricao(), ponto.getIndice().getValor(), ponto.getHorasPermanencia().toSeconds(), ponto.pedidoAlteracaoPendente());
    }

    public static PontoNovoResponse toNovoResponse(Ponto ponto) {
        return new PontoNovoResponse(ponto.getId().getUsuario().getMatricula(), ponto.getId().getDia(),
                ponto.getDescricao(), ponto.getIndice().getValor(), ponto.getHorasPermanencia().toSeconds(), ponto.pedidoAlteracaoPendente());
    }

    public static PontoAtualizadoResponse toAtualizadoReponse(Ponto ponto) {
        return new PontoAtualizadoResponse(ponto.getId().getUsuario().getMatricula(), ponto.getId().getDia(),
                ponto.getDescricao(), ponto.getIndice().getValor(), ponto.getHorasPermanencia().toSeconds(), ponto.pedidoAlteracaoPendente());
    }

}
