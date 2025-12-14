package br.jus.trf1.sipe.folha.domain.model;

import br.jus.trf1.sipe.ponto.Ponto;
import br.jus.trf1.sipe.servidor.domain.model.Servidor;
import br.jus.trf1.sipe.usuario.domain.port.out.UsuarioSecurityPort;
import br.jus.trf1.sipe.usuario.exceptions.UsuarioNaoAutorizadoException;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Folha {

    private FolhaId id;

    private Timestamp dataAbertura;

    private Servidor servidorHomologador;

    private Timestamp dataHomologacao;

    private List<Ponto> pontos;


    public void homologar(Servidor servidor, UsuarioSecurityPort usuarioSecurityPort) {
        if (usuarioSecurityPort.ehDiretor()) {
            this.servidorHomologador = servidor;
            this.dataHomologacao = new Timestamp(System.currentTimeMillis());
        }
        throw new UsuarioNaoAutorizadoException("Somente Diretor pode homologar uma folha!");
    }
}
