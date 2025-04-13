package br.jus.trf1.sipe.erro;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ErroValidacao extends Erro {

    private List<ErroParametro> erros = new ArrayList<>();

    public ErroValidacao(Integer status, String mensagem, Long timeStamp, String path) {
        super(status, mensagem, timeStamp, path);
    }

    public void addError(ErroParametro erroParametro) {
        this.erros.add(erroParametro);
    }

    public void addError(String parametro, String mensagem) {
        addError(new ErroParametro(parametro, mensagem));
    }

}
