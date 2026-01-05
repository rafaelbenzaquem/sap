package br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.model.PedidoAlteracao;
import br.jus.trf1.sipe.registro.domain.model.Registro;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AlteracaoRegistro {

    private Long id;

    private Acao acao;

    private PedidoAlteracao pedidoAlteracao;

    private Registro registroOriginal;

    private Registro registroNovo;

}