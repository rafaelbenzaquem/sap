package br.jus.trf1.sipe.arquivo;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "arquivos",schema = "sispontodb")
public class Arquivo {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    @Column(unique = true)
    private String nome;
    @Column(columnDefinition="blob")
    private byte[] bytes;
    @Column(columnDefinition="text")
    private String descricao;

}
