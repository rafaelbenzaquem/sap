package br.jus.trf1.sipe.externo.jsarh.servidor;

import br.jus.trf1.sipe.externo.jsarh.lotacao.LotacaoExterna;
import br.jus.trf1.sipe.externo.jsarh.lotacao.dto.LotacaoExternaResponse;
import br.jus.trf1.sipe.externo.jsarh.servidor.dto.ServidorExternoResponse;
import br.jus.trf1.sipe.servidor.Servidor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Builder
@AllArgsConstructor
@Getter
public class ServidorExterno {

    private final String matricula;

    private final String nome;

    private final String email;

    private final String funcao;

    private final String cargo;

   private final LotacaoExterna lotacao;



   public static ServidorExterno from(ServidorExternoResponse servidorExternoResponse, LotacaoExternaResponse lotacaoExternaResponse) {
       return ServidorExterno.builder()
               .matricula(servidorExternoResponse.getMatricula())
               .nome(servidorExternoResponse.getNome())
               .email(servidorExternoResponse.getEmail())
               .funcao(servidorExternoResponse.getFuncao())
               .cargo(servidorExternoResponse.getCargo())
               .lotacao(LotacaoExterna.from(lotacaoExternaResponse))
               .build();
   }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ServidorExterno that)) return false;
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
