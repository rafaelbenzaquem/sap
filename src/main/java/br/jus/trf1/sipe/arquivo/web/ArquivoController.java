package br.jus.trf1.sipe.arquivo.web;

import br.jus.trf1.sipe.arquivo.ArquivoService;
import br.jus.trf1.sipe.arquivo.db.Arquivo;
import br.jus.trf1.sipe.arquivo.db.ArquivoRepository;
import br.jus.trf1.sipe.arquivo.exceptions.ArquivoExistenteException;
import br.jus.trf1.sipe.arquivo.exceptions.ArquivoInexistenteException;
import br.jus.trf1.sipe.arquivo.web.dto.ArquivoAtualizadoRequest;
import br.jus.trf1.sipe.arquivo.web.dto.ArquivoMetadataResponse;
import br.jus.trf1.sipe.arquivo.web.dto.ArquivoNovoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequestMapping("/v1/sipe/arquivos")
@RestController
public class ArquivoController {


    private final ArquivoService arquivoService;

    public ArquivoController(ArquivoService arquivoService) {
        this.arquivoService = arquivoService;
    }

    @PostMapping
    public ResponseEntity<EntityModel<ArquivoMetadataResponse>> salvarArquivo(@RequestParam("bytes") MultipartFile conteudo,
                                                                              @RequestParam("nome") String nome,
                                                                              @RequestParam(value = "descricao", required = false) String descricao)
            throws IOException {
        log.info("Salvando um arquivo no banco de dados...");
        log.info("{} - {}KBytes", nome, conteudo.getSize() / 1000);

        ArquivoMetadataResponse metada = arquivoService.armazena(ArquivoNovoRequest.builder()
                .id(UUID.randomUUID().toString())
                .nome(nome)
                .descricao(descricao)
                .bytes(conteudo.getBytes())
                .build());
        var uriResponse = ServletUriComponentsBuilder.fromCurrentContextPath().path("/arquivos/{id}").buildAndExpand(metada.id()).toUri();

        return ResponseEntity.created(uriResponse).body(EntityModel.of(metada, Link.of(uriResponse.toString())));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<ArquivoMetadataResponse>> atualizarArquivo(@PathVariable("id")
                                                                                 String id,
                                                                                 @RequestParam("bytes") MultipartFile conteudo,
                                                                                 @RequestParam("nome") String nome,
                                                                                 @RequestParam(value = "descricao", required = false) String descricao)
            throws IOException {
        log.info("Atualizando um arquivo no banco de dados");
        log.info("{} - {}KBytes", nome, conteudo.getSize() / 1000);

        ArquivoMetadataResponse metada = arquivoService.atualiza(ArquivoAtualizadoRequest.builder()
                .id(id)
                .nome(nome)
                .descricao(descricao)
                .bytes(conteudo.getBytes())
                .build());
        var uriResponse = ServletUriComponentsBuilder.fromCurrentContextPath().path("/arquivos/{id}").buildAndExpand(metada.id()).toUri();

        return ResponseEntity.created(uriResponse).body(EntityModel.of(metada, Link.of(uriResponse.toString())));
    }

}
