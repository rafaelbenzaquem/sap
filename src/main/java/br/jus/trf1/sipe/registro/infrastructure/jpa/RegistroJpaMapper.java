package br.jus.trf1.sipe.registro.infrastructure.jpa;

import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.ponto.infrastructure.jpa.PontoJpa;
import br.jus.trf1.sipe.ponto.infrastructure.jpa.PontoJpaMapper;
import br.jus.trf1.sipe.registro.domain.model.Registro;
import br.jus.trf1.sipe.servidor.domain.model.Servidor;
import br.jus.trf1.sipe.servidor.infrastructure.jpa.ServidorJpa;

public class RegistroJpaMapper {

    private RegistroJpaMapper() {
    }

    public static Registro toDomain(RegistroJpa registroJpa) {
        return Registro.builder()
                .id(registroJpa.getId())
                .codigoAcesso(registroJpa.getCodigoAcesso())
                .hora(registroJpa.getHora())
                .sentido(registroJpa.getSentido())
                .ativo(registroJpa.getAtivo())
                .dataCadastro(registroJpa.getDataCadastro())
                .servidorCriador(registroJpa.getServidorCriador()
                        != null ? Servidor.builder()
                        .id(registroJpa.getServidorCriador().getId())
                        .build() : null)
                .registroNovo(registroJpa.getRegistroNovo()
                        != null ? Registro.builder()
                        .id(registroJpa.getRegistroNovo().getId())
                        .build() : null)
                .ponto(Ponto.builder()
                        .id(PontoJpaMapper.toDomainId(registroJpa.getPonto().getId()))
                        .build())
                .build();
    }

    public static RegistroJpa toEntity(Registro registro) {
        return RegistroJpa.builder()
                .id(registro.getId())
                .codigoAcesso(registro.getCodigoAcesso())
                .hora(registro.getHora())
                .sentido(registro.getSentido().getCodigo())
                .ativo(registro.getAtivo())
                .dataCadastro(registro.getDataCadastro())
                .servidorCriador(registro.getServidorCriador()
                        != null ? ServidorJpa.builder()
                        .id(registro.getServidorCriador().getId())
                        .build() : null)
                .registroNovo(registro.getRegistroNovo()
                        != null ? RegistroJpa.builder()
                        .id(registro.getRegistroNovo().getId())
                        .build() : null)
                .ponto(registro.getPonto() == null ? null : PontoJpa.builder()
                        .id(PontoJpaMapper.toEntityId(registro.getPonto().getId()))
                        .build())

                .build();
    }
}
