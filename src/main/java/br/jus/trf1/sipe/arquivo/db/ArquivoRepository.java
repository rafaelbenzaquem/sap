package br.jus.trf1.sipe.arquivo.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArquivoRepository extends JpaRepository<Arquivo, String> {

    Optional<Arquivo> findByNome(String nome);


    /**
     * Verifica a existência usando uma consulta JPQL explícita.
     * Retorna diretamente um boolean fazendo a comparação > 0 no JPQL.
     * Útil para consultas mais complexas ou quando a convenção de nome não se aplica bem.
     *
     * @param nome O nome a ser verificado.
     * @param id Identificador o Arquivo que faz a requisição
     * @return true se um Arquivo com o nome existir, false caso contrário.
     */
    @Query("SELECT COUNT(a.id) > 0 FROM Arquivo a WHERE a.nome = :nomeParam AND a.id != :idParam")
    boolean checaSeExisteArquivoComNome(@Param("nomeParam") String nome, @Param("idParam") String id);

}
