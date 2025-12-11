package br.jus.trf1.sipe.arquivo.application.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.Arrays;
import java.util.Objects;

@Builder
public record ArquivoAtualizadoRequest(@NotBlank(message = "Campo 'id' não pode ser nulo ou em branco!")
                                       String id,
                                       String nome,
                                       @NotBlank(message = "Campo 'tipoDeConteudo' não pode ser nulo ou em branco!")
                                       String tipoDeConteudo,
                                       byte[] bytes,
                                       String descricao) {


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ArquivoAtualizadoRequest that)) return false;
        return Objects.equals(id, that.id) && Objects.deepEquals(bytes, that.bytes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Arrays.hashCode(bytes));
    }

    @Override
    public String toString() {
        return "ArquivoAtualizadoRequest{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", tipoDeConteudo='" + tipoDeConteudo + '\'' +
                ", bytes=" + Arrays.toString(bytes) +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
