package br.jus.trf1.sipe.folha.infrastructure.jpa;

import br.jus.trf1.sipe.folha.domain.model.Mes;
import br.jus.trf1.sipe.servidor.infrastructure.jpa.ServidorJpa;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Embeddable
public class FolhaJpaId {

    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "id_servidor", referencedColumnName = "id", nullable = false, updatable = false),
    }, foreignKey = @ForeignKey(name = "fk_servidor_folha"))
    private ServidorJpa servidor;

    @Column(nullable = false, length = 2)
    private Integer mes;

    @Column(nullable = false, length = 4)
    private Integer ano;



    public Mes getMes() {
        return Mes.getMes(mes);
    }

    public void setMes(Mes mes) {
        this.mes = mes.getValor();
    }

}
