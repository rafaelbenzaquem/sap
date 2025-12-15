package br.jus.trf1.sipe.feriado.domain.model;

import lombok.*;

import java.time.LocalDate;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class  Feriado {

    private LocalDate data;

    private String descricao;

    private Integer abrangencia;

    private Integer tipo;

}
