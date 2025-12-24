package br.jus.trf1.sipe.arquivo.application.web.dto;

import lombok.Builder;

import java.util.Arrays;
import java.util.Objects;

@Builder
public record ArquivoResponse(String id,
                              String nome,
                              String tipoDeConteudo,
                              byte[] bytes,
                              String descricao) {
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ArquivoResponse that)) return false;

        return Objects.equals(id, that.id) && Arrays.equals(bytes, that.bytes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Arrays.hashCode(bytes);
        return result;
    }

    @Override
    public String toString() {
        return "ArquivoResponse{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", tipoDeConteudo='" + tipoDeConteudo + '\'' +
                ", bytes=" + Arrays.toString(bytes) +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
