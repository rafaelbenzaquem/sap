package br.jus.trf1.sipe.externo.jsarh.feriado;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class FeriadoExternal {

    private LocalDate data;

    private String descricao;

    private Integer abrangencia;

    private Integer tipo;
}
