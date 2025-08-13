package br.jus.trf1.sipe.registro.web;

import br.jus.trf1.sipe.alteracao.pedido_alteracao.PedidoAlteracaoService;
import br.jus.trf1.sipe.ponto.PontoService;
import br.jus.trf1.sipe.registro.RegistroService;
import br.jus.trf1.sipe.registro.web.dto.RegistroResponse;
import br.jus.trf1.sipe.usuario.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/v1/sipe/registros")
public class RegistroDeleteController {

    private final RegistroService registroService;
    private final PontoService pontoService;
    private final PedidoAlteracaoService pedidoAlteracaoService;
    private final UsuarioService usuarioService;

    public RegistroDeleteController(RegistroService registroService, PontoService pontoService,
                                    PedidoAlteracaoService pedidoAlteracaoService, UsuarioService usuarioService) {
        this.registroService = registroService;
        this.pontoService = pontoService;
        this.pedidoAlteracaoService = pedidoAlteracaoService;
        this.usuarioService = usuarioService;
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('GRP_SIPE_USERS')")
    public ResponseEntity<String> apagar(@PathVariable("id") Long id, @RequestParam("justificativa") String justificativa) {

        log.info("apagando registro {}", id);
        var usuarioAtual = usuarioService.getUsuarioAtual();
        var registro = registroService.buscaRegistroPorId(id);
        var ponto = registro.getPonto();
        var matricula = ponto.getId().getMatricula();
        var dia = ponto.getId().getDia();
        var pedidoAlteracao = pedidoAlteracaoService.buscaPedidoAlteracao(matricula, dia)
                .orElse(pedidoAlteracaoService.criarPedidoAlteracao(ponto, "", usuarioAtual));

        pedidoAlteracao.setJustificativa(justificativa);

        registroService.apagar(id);
        return ResponseEntity.ok("Registro id :" + id + " apagado com sucesso!");
    }

}
