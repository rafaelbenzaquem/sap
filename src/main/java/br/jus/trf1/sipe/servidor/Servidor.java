package br.jus.trf1.sipe.servidor;

import br.jus.trf1.sipe.servidor.ausencia.Ausencia;
import br.jus.trf1.sipe.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Servidor extends Usuario {

    private String email;

    private String funcao;

    private String cargo;

    private String siglaLotacao;

    private String descricaoLotacao;

    @OneToMany(mappedBy = "servidor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Ausencia> ausencias;

    public Servidor(Integer id, String nome, String matricula, String cracha, Integer horaDiaria, String email, String funcao, String cargo, String siglaLotacao, String descricaoLotacao) {
        super(id, nome, matricula, cracha, horaDiaria);
        this.email = email;
        this.funcao = funcao;
        this.cargo = cargo;
        this.siglaLotacao = siglaLotacao;
        this.descricaoLotacao = descricaoLotacao;
    }

    public Servidor(Usuario usuario, String email, String funcao, String cargo, String siglaLotacao, String descricaoLotacao) {
        super(usuario.getId(), usuario.getNome(), usuario.getMatricula(), usuario.getCracha(), usuario.getHoraDiaria());
        this.email = email;
        this.funcao = funcao;
        this.cargo = cargo;
        this.siglaLotacao = siglaLotacao;
        this.descricaoLotacao = descricaoLotacao;
    }
}
