package br.jus.trf1.sipe.feriado.infrastructure.jpa;

import br.jus.trf1.sipe.feriado.domain.model.Feriado;

import java.util.Objects;

public class FeriadoJpaMapper {

    private FeriadoJpaMapper() {
    }

    public static Feriado toDomain(FeriadoJpa feriadoJpa) {
        Objects.requireNonNull(feriadoJpa);
        return Feriado.builder()
                .data(feriadoJpa.getData())
                .tipo(feriadoJpa.getTipo())
                .abrangencia(feriadoJpa.getAbrangencia())
                .descricao(feriadoJpa.getDescricao())
                .build();
    }

    public static FeriadoJpa toEntity(Feriado feriado) {
        Objects.requireNonNull(feriado);
        return FeriadoJpa.builder()
                .data(feriado.getData())
                .tipo(feriado.getTipo())
                .abrangencia(feriado.getAbrangencia())
                .descricao(feriado.getDescricao())
                .build();
    }
}
