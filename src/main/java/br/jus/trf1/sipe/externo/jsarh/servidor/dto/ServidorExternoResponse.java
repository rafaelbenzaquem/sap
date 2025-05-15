package br.jus.trf1.sipe.externo.jsarh.servidor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

@Builder
@AllArgsConstructor
@Getter
public class ServidorExternoResponse extends RepresentationModel<ServidorExternoResponse> {

    private final String matricula;

    private final String nome;

    private final String email;

    private final String funcao;

    private final String cargo;

    @JsonProperty(value = "sigla_lotacao")
    private final String siglaLotacao;

    @JsonProperty(value = "descricao_lotacao")
    private final String descricaoLotacao;


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ServidorExternoResponse that = (ServidorExternoResponse) o;
        return Objects.equals(matricula, that.matricula) && Objects.equals(nome, that.nome) && Objects.equals(email, that.email) && Objects.equals(funcao, that.funcao) && Objects.equals(cargo, that.cargo) && Objects.equals(siglaLotacao, that.siglaLotacao) && Objects.equals(descricaoLotacao, that.descricaoLotacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), matricula, nome, email, funcao, cargo, siglaLotacao, descricaoLotacao);
    }

    @Override
    public String toString() {
        return """
                {
                    "matricula":%s,
                    "nome": %s,
                    "email": %s,
                    "funcao": %s,
                    "cargo": %s,
                    "sigla_lotacao": %s,
                    "descricao_lotacao" %s
                }
               """.formatted(matricula == null ? null : "\"" + matricula + "\"",
                nome == null ? null : "\"" + nome + "\"",
                email == null ? null : "\"" + email + "\"",
                funcao == null ? null : "\"" + funcao + "\"",
                cargo == null ? null : "\"" + cargo + "\"",
                siglaLotacao == null ? null : "\"" + siglaLotacao + "\"",
                descricaoLotacao == null ? null : "\"" + descricaoLotacao + "\"");
    }
}
