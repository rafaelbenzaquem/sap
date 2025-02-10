package br.jus.trf1.sap.arquivo.web;

import br.jus.trf1.sap.arquivo.Arquivo;
import br.jus.trf1.sap.arquivo.ArquivoInexistenteException;
import br.jus.trf1.sap.arquivo.ArquivoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequestMapping("/v1/sap/imagens")
@RestController
public class ArquivoController {


    private final ArquivoRepository arquivoRepository;

    public ArquivoController(ArquivoRepository arquivoRepository) {
        this.arquivoRepository = arquivoRepository;
    }

    @PostMapping
    public ResponseEntity<?> salvarArquivo(@RequestParam("conteudo") MultipartFile conteudo,
                                           @RequestParam("nome") String nome,
                                           @RequestParam(value = "descricao", required = false) String descricao)
            throws IOException {

        log.info("Salvando um arquivo no banco de dados");
        log.info("{} - {}KBytes", nome, conteudo.getSize() / 1000);
        Arquivo arquivo = arquivoRepository.save(Arquivo.builder()
                .nome(nome)
                .descricao(descricao)
                .conteudo(conteudo.getBytes())
                .build());
        var uriResponse = ServletUriComponentsBuilder.fromCurrentContextPath().path("/imagens/{id}").buildAndExpand(arquivo.getId()).toUri();
        return ResponseEntity.created(uriResponse).build();
    }

    @PatchMapping
    public ResponseEntity<?> atualizarArquivo(@RequestParam("conteudo") MultipartFile conteudo,
                                              @RequestParam("nome") String nome,
                                              @RequestParam(value = "descricao", required = false) String descricao)
            throws IOException {

        log.info("Atualizando um arquivo no banco de dados");
        log.info("{} - {}KBytes", nome, conteudo.getSize() / 1000);

        Optional<Arquivo> arquivoOpt = arquivoRepository.findByNome(nome);

        if (arquivoOpt.isPresent()) {
            Arquivo arquivo = arquivoRepository.save(Arquivo.builder()
                    .id(arquivoOpt.get().getId())
                    .nome(nome)
                    .descricao(descricao)
                    .conteudo(conteudo.getBytes())
                    .build());
            var uriResponse = ServletUriComponentsBuilder.fromCurrentContextPath().path("/imagens/{id}").buildAndExpand(arquivo.getId()).toUri();
            return ResponseEntity.created(uriResponse).build();
        }

        throw new ArquivoInexistenteException("Não existe arquivo com nome '" + nome + "' salvo no banco de dados!");
    }

}
