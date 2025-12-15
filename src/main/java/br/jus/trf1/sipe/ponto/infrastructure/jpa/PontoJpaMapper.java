package br.jus.trf1.sipe.ponto.infrastructure.jpa;

import br.jus.trf1.sipe.folha.infrastructure.jpa.FolhaJpaMapper;
import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.ponto.domain.model.PontoId;
import br.jus.trf1.sipe.usuario.infrastructure.jpa.UsuarioJpaMapper;

public class PontoJpaMapper {

    private PontoJpaMapper() {
    }

    public static Ponto toDomain(PontoJpa pontoJpa) {
        return Ponto.builder()
                .id(toDomainId(pontoJpa.getId()))
                .indice(pontoJpa.getIndice())
                .descricao(pontoJpa.getDescricao())
                .registros(pontoJpa.getRegistros())
                .pedidos(pontoJpa.getPedidos())
                .folha(pontoJpa.getFolha() == null ? null : FolhaJpaMapper.toDomain(pontoJpa.getFolha()))
                .build();
    }

    public static PontoId toDomainId(PontoJpaId pontoJpaId) {
        return PontoId.builder()
                .dia(pontoJpaId.getDia())
                .usuario(UsuarioJpaMapper.toDomain(pontoJpaId.getUsuario()))
                .build();
    }


    public static PontoJpa toEntity(Ponto ponto) {
        return PontoJpa.builder()
                .id(toEntityId(ponto.getId()))
                .indice(ponto.getIndice().getValor())
                .descricao(ponto.getDescricao())
                .registros(ponto.getRegistros())
                .pedidos(ponto.getPedidos())
                .folha(ponto.getFolha() == null ? null : FolhaJpaMapper.toEntity(ponto.getFolha()))
                .build();
    }

    public static PontoJpaId toEntityId(PontoId pontoId) {
        return PontoJpaId.builder()
                .dia(pontoId.getDia())
                .usuario(UsuarioJpaMapper.toEntity(pontoId.getUsuario()))
                .build();
    }


}
