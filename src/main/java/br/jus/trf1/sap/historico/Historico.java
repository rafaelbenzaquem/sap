package br.jus.trf1.sap.historico;

import br.jus.trf1.sap.historico.dto.HistoricoResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Historico {

    private final Integer acesso;
    @JsonProperty(value = "data_hora")
    private final LocalDateTime dataHora;
    @JsonProperty(value = "id_pedestre")
    private final Integer idPedestre;
    private final String cracha;
    private final Character sentido;

    public static Historico of(HistoricoResponse response) {

        return new Historico(response.acesso(), response.dataHora(), response.idPedestre(),
                response.cracha(), response.sentido());
    }
}
