package br.jus.trf1.sipe.erro;

import lombok.Builder;


@Builder
public record ErroParametro(String parametro,
                            String mensagem) {

}
