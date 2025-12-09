package br.jus.trf1.sipe.lotacao.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "sisponto", name = "lotacoes")
public class Lotacao {

    @Id
    private Integer id;

    private String sigla;

    private String descricao;

    @ManyToOne
    @JoinColumn(name = "id_lotacao_pai")
    private Lotacao lotacaoPai;

    @OneToMany(mappedBy = "lotacaoPai", fetch = FetchType.LAZY)
    private List<Lotacao> subLotacoes;
}