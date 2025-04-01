package br.jus.trf1.sap.erro;

import br.jus.trf1.sap.registro.exceptions.RegistroExistenteSalvoEmPontoDifenteException;
import br.jus.trf1.sap.registro.exceptions.RegistroInexistenteException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class RegistroExceptionHandler {

    @ExceptionHandler(RegistroExistenteSalvoEmPontoDifenteException.class)
    public ResponseEntity<Erro> registroExistenteExceptionHandler(RegistroExistenteSalvoEmPontoDifenteException ex,
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
