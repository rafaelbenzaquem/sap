package br.jus.trf1.sipe.arquivo.db;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "arquivos", schema = "sispontodb",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nome"}, name = "uk_arquivo_nome")
})
public class Arquivo {

    @Id
    private String id;
    @Column(unique = true, nullable = false)
    private String nome;
    @Column(name = "tipo_de_conteudo", nullable = false)
    private String tipoDeConteudo;
    @Column(columnDefinition = "blob", nullable = false)
    private byte[] bytes;
    @Column(columnDefinition = "text")
    private String descricao;

}
