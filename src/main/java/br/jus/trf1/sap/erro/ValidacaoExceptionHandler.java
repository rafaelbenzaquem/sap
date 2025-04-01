package br.jus.trf1.sap.erro;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@ControllerAdvice
public class ValidacaoExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Erro> validacaoException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        var error = new ErroValidacao(HttpStatus.BAD_REQUEST.value(),
                "Erro de validação.",
                System.currentTimeMillis(),
                request.getRequestURI());
        ex.getBindingResult().getFieldErrors().forEach(f -> error.addError(f.getField(), f.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Erro> validacaoException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        log.warn("MethodArgumentTypeMismatchException: parâmetro '{}'", ex.getName());

        var msg = "Parâmetro '%s' não está no formato correto".formatted(ex.getName());
        msg += mensagemErroParametro(ex.getParameter().getParameter().getType().getSimpleName());
        Erro error = new Erro(HttpStatus.BAD_REQUEST.value(),
                msg,
                System.currentTimeMillis(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Erro> validacaoException(HttpMessageNotReadableException ex, HttpServletRequest request) {

        log.warn("HttpMessageNotReadableException: mensagem '{}'", ex.getMessage());

        Erro error = new Erro(HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                System.currentTimeMillis(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    private String mensagemErroParametro(String nomeClasse) {
        switch (nomeClasse) {
            case "LocalDate":
                return ": ddMMyyyy";
            default:
                return "";
        }
    }
}
