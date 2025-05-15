package br.jus.trf1.sipe.servidor.ausencia;

import br.jus.trf1.sipe.servidor.Servidor;
import br.jus.trf1.sipe.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "ausencia_servidor",schema = "sispontodb")
public class Ausencia {

    @Id
    private Long id;
    private LocalDate inicio;
    private LocalDate fim;
    private String descricao;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumns(value = {
            @JoinColumn(name = "servidor_id", referencedColumnName = "id", nullable = false)
    }, foreignKey = @ForeignKey(name = "fk_ausencia_servidor"))
    private Usuario servidor;

}
