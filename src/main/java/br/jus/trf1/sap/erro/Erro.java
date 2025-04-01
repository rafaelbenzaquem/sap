package br.jus.trf1.sap.erro;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class Erro {

    @JsonProperty("status_code")
    private final Integer statusCode;
    private final String mensagem;
    private final Long timestamp;
    private final String path;

}
