package br.jus.trf1.sipe.servidor.application.jsarh.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

@Builder
@AllArgsConstructor
@Getter
public class ServidorJSarhResponse extends RepresentationModel<ServidorJSarhResponse> {

    private final String matricula;

    private final String nome;

    private final String email;

    private final String funcao;

    private final String cargo;

    @JsonProperty(value = "id_lotacao")
    private final Integer idLotacao;


    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ServidorJSarhResponse that)) return false;
        if (!super.equals(o)) return false;

        return Objects.equals(matricula, that.matricula);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(matricula);
        return result;
    }

    @Override
    public String toString() {
        return "ServidorExternoResponse{" +
                "matricula='" + matricula + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", funcao='" + funcao + '\'' +
                ", cargo='" + cargo + '\'' +
                ", idLotacao=" + idLotacao +
                '}';
    }
}
