package br.jus.trf1.sipe.arquivo.web.dto;

import br.jus.trf1.sipe.arquivo.db.Arquivo;
import br.jus.trf1.sipe.comum.validadores.Unico;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.Arrays;
import java.util.Objects;

@Builder
public record ArquivoNovoRequest(@NotBlank(message = "Campo 'id' não pode ser nulo ou em branco!")
                                 String id,
                                 @Unico(domainClass = Arquivo.class,
                                         fieldName = "id",
                                         message = "Já existe um arquivo salvo com esse nome!"
                                 ) String nome,
                                 @NotBlank(message = "Campo 'tipoDeConteudo' não pode ser nulo ou em branco!")
                                 String tipoDeConteudo,
                                 byte[] bytes,
                                 String descricao) {

    public Arquivo toModel() {
        return Arquivo.builder()
                .id(id)
                .nome(nome)
                .tipoDeConteudo(tipoDeConteudo)
                .bytes(bytes)
                .descricao(descricao)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ArquivoNovoRequest that)) return false;
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
        return "ArquivoNovoRequest{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", tipoDeConteudo='" + tipoDeConteudo + '\'' +
                ", bytes=" + Arrays.toString(bytes) +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
