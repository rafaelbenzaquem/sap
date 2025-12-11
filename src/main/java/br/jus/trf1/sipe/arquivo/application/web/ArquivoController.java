package br.jus.trf1.sipe.arquivo.application.web;

import br.jus.trf1.sipe.arquivo.application.web.dto.ArquivoListResponse;
import br.jus.trf1.sipe.arquivo.application.web.dto.ArquivoMetadataResponse;
import br.jus.trf1.sipe.arquivo.domain.model.Arquivo;
import br.jus.trf1.sipe.arquivo.domain.port.in.ArquivoServicePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static br.jus.trf1.sipe.arquivo.application.web.ArquivoWebMapper.toMetadataResponse;

@Slf4j
@RequestMapping("/v1/sipe/arquivos")
@RestController
public class ArquivoController {


    private final ArquivoServicePort arquivoServicePort;

    public ArquivoController(ArquivoServicePort arquivoServicePort) {
        this.arquivoServicePort = arquivoServicePort;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('GRP_SIPE_ADMIN')")
    public ResponseEntity<EntityModel<ArquivoMetadataResponse>> salvarArquivo(@RequestParam("bytes") MultipartFile conteudo,
                                                                              @RequestParam("nome") String nome,
                                                                              @RequestParam(value = "descricao", required = false) String descricao)
            throws IOException {
        log.info("Salvando um arquivo no banco de dados...");
        log.info("{} - {}KBytes", nome, conteudo.getSize() / 1000);

        var arquivo = arquivoServicePort.armazena(Arquivo.builder()
                .id(UUID.randomUUID().toString())
                .nome(nome)
                .tipoDeConteudo(conteudo.getContentType())
                .bytes(conteudo.getBytes())
                .descricao(descricao)
                .build());
        var uriResponse = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/v1/sipe/arquivos/{id}")
                .buildAndExpand(arquivo.getId())
                .toUri();
        return ResponseEntity.created(uriResponse).body(EntityModel.of(toMetadataResponse(arquivo),
                Link.of(uriResponse.toString())));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('GRP_SIPE_ADMIN')")
    public ResponseEntity<EntityModel<ArquivoMetadataResponse>> atualizarArquivo(@PathVariable("id")
                                                                                 String id,
                                                                                 @RequestParam("bytes") MultipartFile conteudo,
                                                                                 @RequestParam("nome") String nome,
                                                                                 @RequestParam(value = "descricao", required = false) String descricao)
            throws IOException {
        log.info("Atualizando um arquivo no banco de dados");
        log.info("{} - {}KBytes", nome, conteudo.getSize() / 1000);

        var arquivo = arquivoServicePort.atualiza(Arquivo.builder()
                .id(id)
                .nome(nome)
                .tipoDeConteudo(conteudo.getContentType())
                .bytes(conteudo.getBytes())
                .descricao(descricao)
                .build());
        var uriResponse = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/v1/sipe/arquivos/{id}")
                .buildAndExpand(arquivo.getId())
                .toUri();

        return ResponseEntity.created(uriResponse).body(EntityModel.of(toMetadataResponse(arquivo), Link.of(uriResponse.toString())));
    }
    
    // Listagem de arquivos com paginação
    @GetMapping
    @PreAuthorize("hasAuthority('GRP_SIPE_ADMIN')")
    public ResponseEntity<List<ArquivoListResponse>> listar(@RequestParam(name = "pag", defaultValue = "0") int pag,
                                                            @RequestParam(name = "tamanho", defaultValue = "10") int tamanho) {
        var arquivos = arquivoServicePort.lista(pag, tamanho);
        return ResponseEntity.ok(arquivos.stream().map(ArquivoWebMapper::toListResponse).toList());
    }
    
    // Recupera conteúdo do arquivo (bytes) por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('GRP_SIPE_ADMIN')")
    public ResponseEntity<byte[]> getConteudoPorId(@PathVariable String id) {
        var arquivo = arquivoServicePort.recuperaPorId(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(arquivo.getTipoDeConteudo()))
                .body(arquivo.getBytes());
    }
    
    // Recupera metadata do arquivo por ID
    @GetMapping("/{id}/metadata")
    @PreAuthorize("hasAuthority('GRP_SIPE_ADMIN')")
    public ResponseEntity<ArquivoMetadataResponse> getMetadataPorId(@PathVariable String id) {
        var arquivo = arquivoServicePort.recuperaPorId(id);
        return ResponseEntity.ok(toMetadataResponse(arquivo));
    }
    
    // Recupera e serve conteúdo do arquivo por nome
    @GetMapping("/nome/{nome}")
    public ResponseEntity<byte[]> getConteudoPorNome(@PathVariable String nome) {
        var arquivo = arquivoServicePort.recuperaPorNome(nome);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(arquivo.getTipoDeConteudo()))
                .body(arquivo.getBytes());
    }
    
    // Recupera metadata do arquivo por nome
    @GetMapping("/nome/{nome}/metadata")
    @PreAuthorize("hasAuthority('GRP_SIPE_ADMIN')")
    public ResponseEntity<ArquivoMetadataResponse> getMetadataPorNome(@PathVariable String nome) {
        var arquivo = arquivoServicePort.apagaPorNome(nome);
        return ResponseEntity.ok(toMetadataResponse(arquivo));
    }
    
    // Remove arquivo por ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('GRP_SIPE_ADMIN')")
    public ResponseEntity<ArquivoMetadataResponse> deletePorId(@PathVariable String id) {
        var arquivo = arquivoServicePort.apagaPorId(id);
        return ResponseEntity.ok(toMetadataResponse(arquivo));
    }
    
    // Remove arquivo por nome
    @DeleteMapping("/nome/{nome}")
    @PreAuthorize("hasAuthority('GRP_SIPE_ADMIN')")
    public ResponseEntity<ArquivoMetadataResponse> deletePorNome(@PathVariable String nome) {
        var arquivo = arquivoServicePort.apagaPorNome(nome);
        return ResponseEntity.ok(toMetadataResponse(arquivo));
    }
}
