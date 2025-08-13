package br.jus.trf1.sipe.alteracao.alteracao_registro;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.PedidoAlteracao;
import br.jus.trf1.sipe.alteracao.pedido_alteracao.PedidoAlteracaoService;
import br.jus.trf1.sipe.registro.Registro;
import org.springframework.stereotype.Service;

@Service
public class AlteracaoRegistroService {

    private final AlteracaoRegistroRepository alteracaoRegistroRepository;


    public AlteracaoRegistroService(AlteracaoRegistroRepository alteracaoRegistroRepository) {
        this.alteracaoRegistroRepository = alteracaoRegistroRepository;
    }


    public AlteracaoRegistro salvarAlteracaoNoRegistroDePonto(long idPedidoAlteracao, Long idRegistroOriginal, long idRegistroNovo, Acao acao) {
        AlteracaoRegistro alteracaoRegistro = AlteracaoRegistro.builder()
                .peidoAlteracao(PedidoAlteracao.builder()
                        .id(idPedidoAlteracao)
                        .build())
                .registroOriginal(idRegistroOriginal == null ? null : Registro.builder()
                        .id(idRegistroOriginal)
                        .build())
                        .registroNovo(Registro.builder()
                                .id(idRegistroNovo)
                                .build())
                        .acao(acao)
                        .build();

        return alteracaoRegistroRepository.save(alteracaoRegistro);
    }

    public void apagar(Long id) {
        alteracaoRegistroRepository.findById(id).ifPresent(alteracaoRegistro -> {
            alteracaoRegistroRepository.delete(alteracaoRegistro); });
    }


}
