package br.jus.trf1.sipe.fechamento.domain.port.in;

import br.jus.trf1.sipe.fechamento.domain.model.Fechamento;

public interface FechamentoServicePort {

    Fechamento fecharFolha(String matricula, Integer valorMes, Integer ano);

}
