package br.jus.trf1.sipe.registro.infrastructure.jpa;

import br.jus.trf1.sipe.registro.domain.model.Registro;
import br.jus.trf1.sipe.servidor.infrastructure.jpa.ServidorJpaMapper;

public class RegistroJpaMapper {

    private RegistroJpaMapper() {
    }

    public static Registro toDomain(RegistroJpa registroJpa) {
        return Registro.builder()
                .id(registroJpa.getId())
                .hora(registroJpa.getHora())
                .sentido(registroJpa.getSentido())
                .ativo(registroJpa.getAtivo())
                .dataCadastro(registroJpa.getDataCadastro())
                .servidorCriador(registroJpa.getServidorCriador()
                        != null ? ServidorJpaMapper.toDomain(registroJpa.getServidorCriador()) : null)
                .registroNovo(registroJpa.getRegistroNovo()
                        != null ? Registro.builder()
                        .id(registroJpa.getRegistroNovo().getId())
                        .build() : null)
                .build();
    }

    public static RegistroJpa toEntity(Registro registro) {
        return RegistroJpa.builder()
                .id(registro.getId())
                .hora(registro.getHora())
                .sentido(registro.getSentido().getCodigo())
                .ativo(registro.getAtivo())
                .dataCadastro(registro.getDataCadastro())
                .servidorCriador(registro.getServidorCriador()
                        != null ? ServidorJpaMapper.toEntity(registro.getServidorCriador()) : null)
                .registroNovo(registro.getRegistroNovo()
                        != null ? RegistroJpa.builder()
                        .id(registro.getRegistroNovo().getId())
                        .build() : null)
                .build();
    }
}
