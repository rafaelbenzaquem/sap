package br.jus.trf1.sipe.erro;

import br.jus.trf1.sipe.registro.exceptions.RegistroExistenteException;
import br.jus.trf1.sipe.registro.exceptions.RegistroInexistenteException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class RegistroExceptionHandler {

    @ExceptionHandler(RegistroExistenteException.class)
    public ResponseEntity<Erro> registroExistenteExceptionHandler(RegistroExistenteException ex,
                                                                  HttpServletRequest request) {
        Erro error = new Erro(HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                System.currentTimeMillis(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RegistroInexistenteException.class)
    public ResponseEntity<Erro> registroInexistenteExceptionHandler(RegistroInexistenteException ex, HttpServletRequest request) {
        Erro error = new Erro(HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                System.currentTimeMillis(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}
