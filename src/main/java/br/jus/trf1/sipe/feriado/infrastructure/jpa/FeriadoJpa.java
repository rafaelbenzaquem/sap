package br.jus.trf1.sipe.feriado.infrastructure.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "feriados",schema = "sispontodb")
public class FeriadoJpa {

    @Id
    private LocalDate data;

    private Integer tipo;

    private String descricao;

    private Integer abrangencia;

}
