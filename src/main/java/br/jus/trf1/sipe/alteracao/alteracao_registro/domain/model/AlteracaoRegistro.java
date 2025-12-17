package br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.model.PedidoAlteracao;
import br.jus.trf1.sipe.registro.Registro;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AlteracaoRegistro {

    private Long id;

    private Acao acao;

    private PedidoAlteracao peidoAlteracao;

    private Registro registroOriginal;

    private Registro registroNovo;

}