package br.jus.trf1.sipe.externo.jsarh.ausencias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class Ausencia {
    private LocalDate inicio;
    private LocalDate fim;
    private String descricao;

}
