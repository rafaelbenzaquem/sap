package br.jus.trf1.sipe.lotacao.aplication.jsarh;

import lombok.Builder;


@Builder
public record LotacaoJSarh(Integer id, String sigla, String descricao, LotacaoJSarh lotacaoPai) {

}
