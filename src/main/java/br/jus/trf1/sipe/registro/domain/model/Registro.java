package br.jus.trf1.sipe.registro.domain.model;

import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.servidor.domain.model.Servidor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class Registro implements Comparable<Registro> {

    private Long id;

    private LocalTime hora;

    private Integer codigoAcesso;

    private Sentido sentido;

    private Boolean ativo;

    private Timestamp dataCadastro;

    private Servidor servidorCriador;

    private Registro registroNovo;

    private List<Registro> registrosAntigos;

    private Servidor servidorAprovador;

    private Timestamp dataAprovacao;

    private Ponto ponto;

    @Override
    public int compareTo(Registro oRegistro) {
        return hora.compareTo(oRegistro.getHora());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Registro registro = (Registro) o;
        return Objects.equals(id, registro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
