package br.jus.trf1.sipe.registro.domain.port.in;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.domain.model.PedidoAlteracao;
import br.jus.trf1.sipe.ponto.domain.model.Ponto;
import br.jus.trf1.sipe.registro.domain.model.Registro;
import br.jus.trf1.sipe.servidor.domain.model.Servidor;

import java.time.LocalDate;
import java.util.List;

public interface RegistroServicePort {

    Registro buscaRegistroPorId(Long id);

    Boolean existe(Long id);

    List<Registro> listarRegistrosAtivosPonto(String matricula, LocalDate dia, boolean todos);

    List<Registro> atualizaRegistrosSistemaDeAcesso(Ponto ponto);

    Registro aprovarRegistro(Long idRegistro);

   List<Registro> addRegistros(PedidoAlteracao pedidoAlteracao, Ponto ponto, List<Registro> registros);

    void removeRegistro(PedidoAlteracao pedidoAlteracao, Ponto ponto, Registro registro);

    Registro addPontoCriador(Registro registro, Ponto ponto, Servidor servidor);

    Registro atualizaRegistro(PedidoAlteracao pedidoAlteracao, Ponto ponto, Registro registroAtualizado);

    Registro apaga(Long idRegistro);
}
