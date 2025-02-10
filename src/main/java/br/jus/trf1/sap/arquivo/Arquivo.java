package br.jus.trf1.sap.arquivo;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Arquivo {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    @Column(unique = true)
    private String nome;
    @Column(columnDefinition="blob")
    private byte[] conteudo;
    @Column(columnDefinition="text")
    private String descricao;

}
