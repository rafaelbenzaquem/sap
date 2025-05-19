package br.jus.trf1.sipe.arquivo.web;

import br.jus.trf1.sipe.arquivo.ArquivoService;
import br.jus.trf1.sipe.arquivo.web.dto.ArquivoAtualizadoRequest;
import br.jus.trf1.sipe.arquivo.web.dto.ArquivoListResponse;
import br.jus.trf1.sipe.arquivo.web.dto.ArquivoMetadataResponse;
import br.jus.trf1.sipe.arquivo.web.dto.ArquivoNovoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
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
                .tipoDeConteudo(conteudo.getContentType())
                .bytes(conteudo.getBytes())
                .descricao(descricao)
                .build());
        var uriResponse = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/v1/sipe/arquivos/{id}")
                .buildAndExpand(metada.id())
                .toUri();

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
                .tipoDeConteudo(conteudo.getContentType())
                .bytes(conteudo.getBytes())
                .descricao(descricao)
                .build());
        var uriResponse = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/v1/sipe/arquivos/{id}")
                .buildAndExpand(metada.id())
                .toUri();

        return ResponseEntity.created(uriResponse).body(EntityModel.of(metada, Link.of(uriResponse.toString())));
    }
    
    // Listagem de arquivos com paginação
    @GetMapping
    public ResponseEntity<Page<ArquivoListResponse>> listar(@RequestParam(name = "pag", defaultValue = "0") int pag,
                                                               @RequestParam(name = "tamanho", defaultValue = "10") int tamanho) {
        var page = arquivoService.lista(pag, tamanho);
        return ResponseEntity.ok(page);
    }
    
    // Recupera conteúdo do arquivo (bytes) por ID
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getConteudoPorId(@PathVariable String id) {
        var arquivo = arquivoService.recuperaPorId(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(arquivo.tipoDeConteudo()))
                .body(arquivo.bytes());
    }
    
    // Recupera metadata do arquivo por ID
    @GetMapping("/{id}/metadata")
    public ResponseEntity<ArquivoMetadataResponse> getMetadataPorId(@PathVariable String id) {
        var metadata = arquivoService.recuperaMetadataPorId(id);
        return ResponseEntity.ok(metadata);
    }
    
    // Recupera e serve conteúdo do arquivo por nome
    @GetMapping("/nome/{nome}")
    public ResponseEntity<byte[]> getConteudoPorNome(@PathVariable String nome) {
        var arquivo = arquivoService.recuperaPorNome(nome);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(arquivo.tipoDeConteudo()))
                .body(arquivo.bytes());
    }
    
    // Recupera metadata do arquivo por nome
    @GetMapping("/nome/{nome}/metadata")
    public ResponseEntity<ArquivoMetadataResponse> getMetadataPorNome(@PathVariable String nome) {
        var metadata = arquivoService.recuperaMetadataPorNome(nome);
        return ResponseEntity.ok(metadata);
    }
    
    // Remove arquivo por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ArquivoMetadataResponse> deletePorId(@PathVariable String id) {
        var metadata = arquivoService.apagaPorId(id);
        return ResponseEntity.ok(metadata);
    }
    
    // Remove arquivo por nome
    @DeleteMapping("/nome/{nome}")
    public ResponseEntity<ArquivoMetadataResponse> deletePorNome(@PathVariable String nome) {
        var metadata = arquivoService.apagaPorNome(nome);
        return ResponseEntity.ok(metadata);
    }
}
