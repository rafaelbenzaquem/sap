package br.jus.trf1.sap.erro;

import lombok.Builder;


@Builder
public record ErroParametro(String parametro,
                            String mensagem) {

}
