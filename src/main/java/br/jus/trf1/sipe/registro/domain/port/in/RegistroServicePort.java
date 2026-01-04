package br.jus.trf1.sipe.registro.domain.port.in;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.model.PedidoAlteracao;
import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.registro.domain.model.Registro;

import java.time.LocalDate;
import java.util.List;

public interface RegistroServicePort {

    Registro buscaRegistroPorId(Long id);

    Boolean existe(Long id);

    List<Registro> listaAtuaisDoPonto(String matricula, LocalDate dia, boolean todos);

    List<Registro> salvaNovosDeSistemaExternoEmBaseInterna(Ponto ponto);

    Registro aprova(Long idRegistro);

    List<Registro> salva(PedidoAlteracao pedidoAlteracao, Ponto ponto, List<Registro> registros);

    void remove(PedidoAlteracao pedidoAlteracao, Ponto ponto, Registro registro);

    Registro atualiza(PedidoAlteracao pedidoAlteracao, Ponto ponto, Registro registroAtualizado);

    Registro apaga(Long idRegistro);

    List<Registro> buscaNovosEmSistemaExterno(String matricula, LocalDate dia);
}
