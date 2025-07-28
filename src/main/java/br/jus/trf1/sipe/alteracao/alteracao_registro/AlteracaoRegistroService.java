package br.jus.trf1.sipe.alteracao.alteracao_registro;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.PedidoAlteracaoService;
import br.jus.trf1.sipe.registro.Registro;
import org.springframework.stereotype.Service;

@Service
public class AlteracaoRegistroService {

    private AlteracaoRegistroRepository alteracaoRegistroRepository;
    private PedidoAlteracaoService pedidoAlteracaoService;


    public AlteracaoRegistroService(AlteracaoRegistroRepository alteracaoRegistroRepository) {
        this.alteracaoRegistroRepository = alteracaoRegistroRepository;
    }


    public AlteracaoRegistro salvar(long idPedidoAlteracao, long idRegistroOriginal, long idRegistroNovo, Acao acao ) {
        AlteracaoRegistro alteracaoRegistro = AlteracaoRegistro.builder()
                .registroOriginal(Registro.builder()
                        .id(idRegistroOriginal)
                        .build())
                .registroNovo(Registro.builder()
                        .id(idRegistroNovo)
                        .build())
                .acao(acao)
                .build();

        return alteracaoRegistroRepository.save(alteracaoRegistro);
    }
}
