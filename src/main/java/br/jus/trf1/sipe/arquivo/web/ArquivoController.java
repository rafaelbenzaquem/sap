package br.jus.trf1.sipe.arquivo.web;

import br.jus.trf1.sipe.arquivo.Arquivo;
import br.jus.trf1.sipe.arquivo.ArquivoRepository;
import br.jus.trf1.sipe.arquivo.exceptions.NomeArquivoExistenteException;
import br.jus.trf1.sipe.arquivo.exceptions.NomeArquivoInexistenteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequestMapping("/v1/sap/arquivos")
@RestController
public class ArquivoController {


    private final ArquivoRepository arquivoRepository;

    public ArquivoController(ArquivoRepository arquivoRepository) {
        this.arquivoRepository = arquivoRepository;
    }

    @PostMapping
    public ResponseEntity<?> salvarArquivo(@RequestParam("bytes") MultipartFile conteudo,
                                           @RequestParam("nome") String nome,
                                           @RequestParam(value = "descricao", required = false) String descricao)
            throws IOException {
        log.info("Salvando um arquivo no banco de dados");
        log.info("{} - {}KBytes", nome, conteudo.getSize() / 1000);

        Optional<Arquivo> arquivoOpt = arquivoRepository.findByNome(nome);
        if (arquivoOpt.isPresent())
            throw new NomeArquivoExistenteException("Existe arquivo com nome '" + nome + "' salvo no banco de dados!");

        Arquivo arquivo = arquivoRepository.save(Arquivo.builder()
                .nome(nome)
                .descricao(descricao)
                .bytes(conteudo.getBytes())
                .build());
        var uriResponse = ServletUriComponentsBuilder.fromCurrentContextPath().path("/arquivos/{id}").buildAndExpand(arquivo.getId()).toUri();
        return ResponseEntity.created(uriResponse).build();
    }

    @PatchMapping
    public ResponseEntity<?> atualizarArquivo(@RequestParam("bytes") MultipartFile conteudo,
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
                    .bytes(conteudo.getBytes())
                    .build());
            var uriResponse = ServletUriComponentsBuilder.fromCurrentContextPath().path("/arquivos/{id}").buildAndExpand(arquivo.getId()).toUri();
            return ResponseEntity.created(uriResponse).build();
        }
        throw new NomeArquivoInexistenteException("Não existe arquivo com nome '" + nome + "' salvo no banco de dados!");
    }

}
