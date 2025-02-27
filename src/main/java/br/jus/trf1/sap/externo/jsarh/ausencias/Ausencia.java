package br.jus.trf1.sap.externo.jsarh.ausencias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class Ausencia {
    private LocalDate inicio;
    private LocalDate fim;
    private String descricao;

}
