package br.jus.trf1.sipe.lotacao.application.jsarh;

import lombok.Builder;


@Builder
public record LotacaoJSarh(Integer id, String sigla, String descricao, LotacaoJSarh lotacaoPai) {

}
