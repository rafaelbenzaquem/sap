package br.jus.trf1.sipe.servidor.application.jsarh.exceptions;

import br.jus.trf1.sipe.erro.Erro;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ServidorExternoExceptionHandler {

    @ExceptionHandler(ServidorExternoInexistenteException.class)
    public ResponseEntity<Erro> servidorNaoEncontradoExceptionHandler(ServidorExternoInexistenteException ex, HttpServletRequest request) {
        Erro error = new Erro(HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                System.currentTimeMillis(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

}
