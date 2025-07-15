package br.jus.trf1.sipe.pedido;

import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.registro.Registro;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedidos_modificaoes_pontos", schema = "sispontodb")
public class PedidoModificaoPonto extends PedidoModificao {

    @OneToOne
    @JoinColumns(value = {
            @JoinColumn(name = "ponto_matricula", referencedColumnName = "matricula", nullable = false),
            @JoinColumn(name = "ponto_dia", referencedColumnName = "dia", nullable = false)
    }, foreignKey = @ForeignKey(name = "fk_ponto_notificao"))
    private Ponto ponto;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "registro")
    private List<Registro> registrosOriginais;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "registro")
    private List<Registro> registrosModificados;

}