package br.jus.trf1.sap.vinculo;

import br.jus.trf1.sap.vinculo.web.dto.VinculoResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

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
    private String matricula;
    @Column(unique = true, nullable = false)
    private String cracha;
    @Min(value = 4)
    @Max(value = 12)
    @Column(name = "hora_diaria", nullable = false)
    private Integer horaDiaria;

    public VinculoResponse toResponse() {
        return new VinculoResponse(id, nome, matricula, cracha, horaDiaria);
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Vinculo vinculo)) return false;

        return Objects.equals(id, vinculo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
