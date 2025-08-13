package br.jus.trf1.sipe.alteracao.pedido_alteracao.exceptions;

import br.jus.trf1.sipe.erro.Erro;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class PedidoAlteracaoExceptionHandler {


    @ExceptionHandler(PedidoAlteracaoInexistenteException.class)
    public ResponseEntity<Erro> pedidoAlteracaoInexistenteException(PedidoAlteracaoInexistenteException ex, HttpServletRequest request) {
        var error = new Erro(HttpStatus.NOT_FOUND.value(),
                "Pedido de alteração não existe!",
                System.currentTimeMillis(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
