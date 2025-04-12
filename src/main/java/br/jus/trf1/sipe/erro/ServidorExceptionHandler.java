package br.jus.trf1.sipe.erro;

import br.jus.trf1.sipe.externo.jsarh.servidor.exceptions.ServidorInexistenteException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ServidorExceptionHandler {

    @ExceptionHandler(ServidorInexistenteException.class)
    public ResponseEntity<Erro> servidorNaoEncontradoExceptionHandler(ServidorInexistenteException ex, HttpServletRequest request) {
        Erro error = new Erro(HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                System.currentTimeMillis(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

}
