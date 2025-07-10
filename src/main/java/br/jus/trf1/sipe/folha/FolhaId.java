package br.jus.trf1.sipe.folha;

import br.jus.trf1.sipe.servidor.Servidor;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Embeddable
public class FolhaId {

    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "id_servidor", referencedColumnName = "id", nullable = false, updatable = false),
    }, foreignKey = @ForeignKey(name = "fk_servidor_folha"))
    private Servidor servidor;

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
