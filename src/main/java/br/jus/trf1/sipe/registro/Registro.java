package br.jus.trf1.sipe.registro;

import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.servidor.Servidor;
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
@Table(name = "registros", schema = "sispontodb")
public final class Registro implements Comparable<Registro> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime hora;

    @Column
    private Integer codigoAcesso;

    private Character sentido;

    @Column
    private Boolean ativo;

    @CreationTimestamp
    private Timestamp dataCadastro;

    @OneToOne
    @JoinColumn(name = "registro_novo_id", foreignKey = @ForeignKey(name = "fk_registro_novo"))
    private Registro registroNovo;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumns(value = {
            @JoinColumn(name = "id_servidor_aprovador", referencedColumnName = "id")
    }, foreignKey = @ForeignKey(name = "fk_aprovador_registro"))
    private Servidor servidorAprovador;

    private Timestamp dataAprovacao;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumns(value = {
            @JoinColumn(name = "ponto_matricula", referencedColumnName = "matricula", nullable = false),
            @JoinColumn(name = "ponto_dia", referencedColumnName = "dia", nullable = false)
    }, foreignKey = @ForeignKey(name = "fk_registro_ponto"))
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
