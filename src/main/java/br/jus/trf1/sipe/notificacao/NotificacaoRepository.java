package br.jus.trf1.sipe.notificacao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {

    Page<Notificacao> findByUsuarioId(Integer usuarioId, Pageable pageable);

    Page<Notificacao> findByUsuarioIdAndFoiLidaFalse(Integer usuarioId, Pageable pageable);

    Long countByUsuarioIdAndFoiLidaFalse(Integer usuarioId);

    List<Notificacao> findByUsuarioIdAndFoiLidaFalseOrderByCreatedAtDesc(Integer usuarioId);

    @Modifying
    @Query("UPDATE Notificacao n SET n.foiLida = true WHERE n.usuario.id = :usuarioId AND n.foiLida = false")
    void marcarTodasComoLidas(@Param("usuarioId") Integer usuarioId);

    @Modifying
    @Query("UPDATE Notificacao n SET n.foiLida = true WHERE n.id = :notificacaoId")
    void marcarComoLida(@Param("notificacaoId") Integer notificacaoId);
}
