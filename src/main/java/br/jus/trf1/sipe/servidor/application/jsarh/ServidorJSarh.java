package br.jus.trf1.sipe.servidor.application.jsarh;

import br.jus.trf1.sipe.lotacao.LotacaoMapping;
import br.jus.trf1.sipe.lotacao.application.jsarh.LotacaoJSarh;
import br.jus.trf1.sipe.lotacao.application.jsarh.dto.LotacaoJSarhResponse;
import br.jus.trf1.sipe.servidor.application.jsarh.dto.ServidorJSarhResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Builder
@AllArgsConstructor
@Getter
public class ServidorJSarh {

    private final String matricula;

    private final String nome;

    private final String email;

    private final String funcao;

    private final String cargo;

   private final LotacaoJSarh lotacao;



   public static ServidorJSarh from(ServidorJSarhResponse servidorExternoResponse, LotacaoJSarhResponse lotacaoExternaResponse) {
       return ServidorJSarh.builder()
               .matricula(servidorExternoResponse.getMatricula())
               .nome(servidorExternoResponse.getNome())
               .email(servidorExternoResponse.getEmail())
               .funcao(servidorExternoResponse.getFuncao())
               .cargo(servidorExternoResponse.getCargo())
               .lotacao(LotacaoMapping.toJSarhDomain(lotacaoExternaResponse))
               .build();
   }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ServidorJSarh that)) return false;
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
        return "ServidorExterno{" +
                "matricula='" + matricula + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", funcao='" + funcao + '\'' +
                ", cargo='" + cargo + '\'' +
                ", lotacao=" + lotacao +
                '}';
    }
}
