package br.jus.trf1.sipe.pedido;

import br.jus.trf1.sipe.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "pedidos_alteracoes", schema = "sispontodb")
public class PedidoAlteracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Acao acao;
    private LocalDateTime dataAcao;

    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_usuario_pedido_alteracao"))
    private Usuario usuario;

}