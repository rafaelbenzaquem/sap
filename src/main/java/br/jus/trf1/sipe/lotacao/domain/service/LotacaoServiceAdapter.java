package br.jus.trf1.sipe.lotacao.domain.service;

import br.jus.trf1.sipe.lotacao.domain.model.Lotacao;
import br.jus.trf1.sipe.lotacao.domain.port.in.LotacaoServicePort;
import br.jus.trf1.sipe.lotacao.domain.port.out.LotacaoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LotacaoServiceAdapter implements LotacaoServicePort {

    private final LotacaoRepositoryPort lotacaoRepositoryPort;

    @Override
    public Set<Integer> getLotacaos(Integer idLotacao) {
        return lotacaoRepositoryPort.buscaLotacoesRecursivas(idLotacao).stream()
                .map(Lotacao::getId)
                .collect(Collectors.toSet());
    }

    @Override
    public void atualizarLotacao(Lotacao lotacaoAtual, Lotacao lotacaoNova) {
        if (((lotacaoAtual == null && lotacaoNova != null)
                || !Objects.equals(lotacaoAtual, lotacaoNova))
                && lotacaoRepositoryPort.buscaPorId(lotacaoNova.getId()).isEmpty()) {
            lotacaoRepositoryPort.salva(lotacaoNova);
        }

    }
}