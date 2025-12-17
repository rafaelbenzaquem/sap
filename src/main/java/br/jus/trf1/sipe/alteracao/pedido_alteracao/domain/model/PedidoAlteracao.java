package br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.model;

import br.jus.trf1.sipe.alteracao.alteracao_registro.domain.model.AlteracaoRegistro;
import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.usuario.domain.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PedidoAlteracao {

    private Long id;

    private Timestamp dataSolicitacao;

    private LocalDateTime dataAprovacao;

    private StatusPedido status;

    private String justificativa;

    private String justificativaAprovador;

    private List<AlteracaoRegistro> alteracaoRegistros;

    private Usuario usuarioSolicitante;

    private Usuario usuarioAprovador;

    private Ponto ponto;

    public void setJustificativa(String justificativa) {
        this.justificativa = this.justificativa ==null? justificativa: this.justificativa +" - "+justificativa;
    }
}