package br.jus.trf1.sipe.fechamento.infastructure.jpa;

import br.jus.trf1.sipe.fechamento.domain.model.Fechamento;

public class FechamentoJpaMapper {

    private FechamentoJpaMapper() {
    }

    public static Fechamento toDomain(FechamentoJpa fechamentoJpa) {
        return Fechamento.builder()
                .id(fechamentoJpa.getId())
                .mes(fechamentoJpa.getMes())
                .ano(fechamentoJpa.getAno())
                .horasExecutadasMinutos(fechamentoJpa.getHorasExecutadasMinutos())
                .horasEsperadasMinutos(fechamentoJpa.getHorasEsperadasMinutos())
                .saldoMinutos(fechamentoJpa.getSaldoMinutos())
                .dataFechamento(fechamentoJpa.getDataFechamento())
                .prazoCompensacao(fechamentoJpa.getPrazoCompensacao())
                .build();
    }

    public static FechamentoJpa toEntity(Fechamento fechamento) {
        return FechamentoJpa.builder()
                .id(fechamento.getId())
                .mes(fechamento.getMes())
                .ano(fechamento.getAno())
                .horasExecutadasMinutos(fechamento.getHorasExecutadasMinutos())
                .horasEsperadasMinutos(fechamento.getHorasEsperadasMinutos())
                .saldoMinutos(fechamento.getSaldoMinutos())
                .dataFechamento(fechamento.getDataFechamento())
                .prazoCompensacao(fechamento.getPrazoCompensacao())
                .build();
    }
}
