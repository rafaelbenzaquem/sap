package br.jus.trf1.sipe.folha;

import br.jus.trf1.sipe.ponto.PontoService;
import br.jus.trf1.sipe.servidor.ServidorService;
import br.jus.trf1.sipe.usuario.UsuarioService;
import org.springframework.stereotype.Service;

@Service
public class FolhaService {

 private final FolhaRepository folhaRepository;
 private final PontoService pontoService;
 private final UsuarioService usuarioService;
 private final ServidorService servidorService;

    public FolhaService(FolhaRepository folhaRepository,
                        PontoService pontoService,
                        UsuarioService usuarioService,
                        ServidorService servidorService) {
        this.folhaRepository = folhaRepository;
        this.pontoService = pontoService;
        this.usuarioService = usuarioService;
        this.servidorService = servidorService;
    }

    public void abrirFolha(String matricula, Mes mes, int ano) {
         usuarioService.buscaPorMatricula(matricula);
    }

}
