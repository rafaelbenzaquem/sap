package br.jus.trf1.sap.ponto.registro;

import br.jus.trf1.sap.ponto.Ponto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Registro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime hora;
    private Integer codigoAcesso;
    private Character sentido;

    private Integer versao;
    private Timestamp dataCadastro;


    @OneToOne
    @JoinColumn(name = "registro_atualizado_id")
    private Registro registroAtualizado;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ponto_matricula", referencedColumnName = "matricula",nullable = false),
            @JoinColumn(name = "ponto_dia", referencedColumnName = "dia",nullable = false)
    })
    private Ponto ponto;

}
