package br.jus.trf1.sipe.lotacao;

import br.jus.trf1.sipe.lotacao.infrastructure.persistence.LotacaoJpa;

import java.util.HashSet;
import java.util.Set;

public class LotacaoUtils {

    public static Set<Integer> coletarIds(LotacaoJpa raiz) {
        Set<Integer> ids = new HashSet<>();
        coletarRecursivo(raiz, ids);
        return ids;
    }

    private static void coletarRecursivo(LotacaoJpa lotacao, Set<Integer> ids) {
        if (lotacao == null || ids.contains(lotacao.getId())) return;

        ids.add(lotacao.getId());

        if (lotacao.getSubLotacoes() != null) {
            for (LotacaoJpa sub : lotacao.getSubLotacoes()) {
                coletarRecursivo(sub, ids);
            }
        }
    }
}
