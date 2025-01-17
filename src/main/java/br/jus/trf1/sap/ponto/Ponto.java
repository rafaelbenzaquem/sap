package br.jus.trf1.sap.ponto;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;



@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Ponto {

    @EmbeddedId
    private PontoId id;

    private String descricao;

    @OneToMany
    private List<Registro> registros;

}
