package br.jus.trf1.sipe.folha;

import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.servidor.Servidor;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
public class Folha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 2)
    private int mes;

    @Column(nullable = false, length = 4)
    private int ano;

    @CreationTimestamp
    private Timestamp dataAbertura;

    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "id_servidor", referencedColumnName = "id", nullable = false, updatable = false),
    }, foreignKey = @ForeignKey(name = "fk_servidor_folha"))
    private Servidor servidor;

    private Timestamp dataHomologacao;
    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "id_servidor_homologador", referencedColumnName = "id", nullable = false, updatable = false),
    }, foreignKey = @ForeignKey(name = "fk_homologador_folha"))
    private Servidor servidorHomologador;

    @OneToMany(mappedBy = "folha", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Ponto> pontos;


    public Mes getMes() {
        return Mes.getMes(mes);
    }

    public void setMes(Mes mes) {
        this.mes = mes.getValor();
    }


    public void homologar(Servidor servidor) {
        if(servidor.getFuncao().contains("DIRETOR")){
            this.servidorHomologador = servidor;
            this.dataHomologacao = new Timestamp(System.currentTimeMillis());
        }
        throw new IllegalArgumentException("Somente Diretor pode homologar folha");
    }
}
