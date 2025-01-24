package br.jus.trf1.sap.ponto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Registro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer acesso;
    private LocalTime hora;
    private Character sentido;

    private Integer versao;

    @OneToOne
    @JoinColumn(name = "registro_atualizado_id")
    private Registro registroAtualizado;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ponto_matricula", referencedColumnName = "matricula"),
            @JoinColumn(name = "ponto_dia", referencedColumnName = "dia")
    })
    private Ponto ponto;

}
