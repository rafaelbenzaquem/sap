package br.jus.trf1.sap.ponto;

import br.jus.trf1.sap.registro.Registro;
import br.jus.trf1.sap.registro.Sentido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ponto {

    @EmbeddedId
    private PontoId id;

    private String descricao;

    @OneToMany(mappedBy = "ponto", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Registro> registros;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Ponto ponto)) return false;

        return Objects.equals(id, ponto.id) && Objects.equals(descricao, ponto.descricao) && Objects.equals(registros, ponto.registros);
    }


    public Duration calculaHorasRegistros() {
        return calcularHoras(this.registros);
    }


    public static Duration calcularHoras(List<Registro> registros) {
        Duration totalHoras = Duration.ZERO;
        LocalTime entradaPendente = null;

        for (Registro registro : registros) {
            if (registro.getSentido() == Sentido.ENTRADA) {
                // Se já há uma entrada pendente, ignora a nova entrada (erro de dupla entrada)
                if (entradaPendente != null) {
                    continue;
                }
                entradaPendente = registro.getHora();
            } else if (registro.getSentido() == Sentido.SAIDA) {
                // Se não há uma entrada pendente, ignora a saída (erro de dupla saída)
                if (entradaPendente == null) {
                    continue;
                }
                // Calcula a diferença entre a entrada pendente e a saída atual
                Duration diferenca = Duration.between(entradaPendente, registro.getHora());
                totalHoras = totalHoras.plus(diferenca);
                entradaPendente = null; // Reseta a entrada pendente após calcular o par
            }
        }

        return totalHoras;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(descricao);
        result = 31 * result + Objects.hashCode(registros);
        return result;
    }

    @Override
    public String toString() {
        return "Ponto{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
