package br.jus.trf1.sap.vinculo;

import br.jus.trf1.sap.vinculo.dto.NovoVinculoRequest;
import br.jus.trf1.sap.vinculo.dto.VinculoResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "servidor_matricula_cracha")
public class Vinculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String nome;
    @Column(unique = true, nullable = false)
    private Integer matricula;
    @Column(unique = true, nullable = false)
    private String cracha;

    public VinculoResponse toResponse() {
        return new VinculoResponse(id,nome,matricula,cracha);
    }

    public static Vinculo of(NovoVinculoRequest novoVinculo) {
        return new Vinculo(null,novoVinculo.nome(),novoVinculo.matricula(), novoVinculo.cracha());
    }
}
