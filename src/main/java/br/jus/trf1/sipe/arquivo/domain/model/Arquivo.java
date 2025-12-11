package br.jus.trf1.sipe.arquivo.domain.model;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Arquivo {

    private String id;
    private String nome;
    private String tipoDeConteudo;
    private byte[] bytes;
    private String descricao;
}
