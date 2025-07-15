package br.jus.trf1.sipe.pedido;

import br.jus.trf1.sipe.usuario.Usuario;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos_modicacoes", schema = "sispontodb")
public class PedidoModificao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Acao acao;
    private LocalDateTime dataAcao;

    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_usuario_pedido_modificao"))
    private Usuario usuario;

}