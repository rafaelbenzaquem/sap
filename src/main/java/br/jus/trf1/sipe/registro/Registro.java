package br.jus.trf1.sipe.registro;

import br.jus.trf1.sipe.ponto.Ponto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Objects;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "registros",schema = "sispontodb")
public final class Registro implements Comparable<Registro> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime hora;
    private Integer codigoAcesso;
    private Character sentido;

    private Integer versao;

    @CreationTimestamp
    private Timestamp dataCadastro;


    @OneToOne
    @JoinColumn(name = "registro_anterior_id",foreignKey = @ForeignKey(name = "fk_registro_anterior"))
    private Registro registroAnterior;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumns(value = {
            @JoinColumn(name = "ponto_matricula", referencedColumnName = "matricula", nullable = false),
            @JoinColumn(name = "ponto_dia", referencedColumnName = "dia", nullable = false)
    },foreignKey = @ForeignKey(name = "fk_registro_ponto"))
    private Ponto ponto;


    public Sentido getSentido() {
        return Sentido.toEnum(sentido);
    }

    public void setSentido(Sentido sentido) {
        this.sentido = sentido.getCodigo();
    }

    @Override
    public int compareTo(Registro oRegistro) {
        return hora.compareTo(oRegistro.getHora());
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Registro registro)) return false;
        return Objects.equals(id, registro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
