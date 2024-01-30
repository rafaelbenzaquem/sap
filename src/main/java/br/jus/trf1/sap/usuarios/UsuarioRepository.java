package br.jus.trf1.sap.usuarios;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Double> {
    Optional<Usuario> findUsuarioByEmailOrCpf(String email, String cpf);
}
