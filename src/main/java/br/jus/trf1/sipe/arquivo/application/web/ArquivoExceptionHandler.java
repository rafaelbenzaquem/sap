package br.jus.trf1.sipe.arquivo.application.web;

import br.jus.trf1.sipe.erro.Erro;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import br.jus.trf1.sipe.arquivo.domain.exceptions.ArquivoInexistenteException;
import br.jus.trf1.sipe.arquivo.domain.exceptions.ArquivoExistenteException;

/**
 * Trata exceções específicas de operações com arquivos, mapeando para respostas HTTP adequadas.
 */
@Slf4j
@ControllerAdvice
public class ArquivoExceptionHandler {

    @ExceptionHandler(ArquivoInexistenteException.class)
    public ResponseEntity<Erro> handleArquivoNaoEncontrado(ArquivoInexistenteException ex,
                                                        HttpServletRequest request) {
        var error = new Erro(HttpStatus.NOT_FOUND.value(),
                              ex.getMessage(),
                              System.currentTimeMillis(),
                              request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ArquivoExistenteException.class)
    public ResponseEntity<Erro> handleArquivoExistente(ArquivoExistenteException ex,
                                                      HttpServletRequest request) {
        var error = new Erro(HttpStatus.BAD_REQUEST.value(),
                              ex.getMessage(),
                              System.currentTimeMillis(),
                              request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
