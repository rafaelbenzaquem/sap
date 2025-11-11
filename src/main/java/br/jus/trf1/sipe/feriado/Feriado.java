package br.jus.trf1.sipe.feriado;

import lombok.Getter;

import java.time.LocalDate;


@Getter
public class  Feriado {

    private LocalDate data;

    private String descricao;

    private Integer abrangencia;

    private Integer tipo;


    public Feriado() {
    }

    public Feriado(LocalDate data, String descricao, Integer abrangencia, Integer tipo) {
        this.data = data;
        this.descricao = descricao;
        this.abrangencia = abrangencia;
        this.tipo = tipo;
    }

}
