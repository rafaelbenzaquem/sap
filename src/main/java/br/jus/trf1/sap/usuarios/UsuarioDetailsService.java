package br.jus.trf1.sap.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuarioOptional = userRepo.findUsuarioByEmailOrCpf(username, username);
        if (usuarioOptional.isPresent()) {
            var usuario = usuarioOptional.get();
            Set<GrantedAuthority> authorities = Stream.of((usuario.getPapel() == 0 ? "ROLE_ADMIN" : "ROLE_USER"))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
            return new org.springframework.security.core.userdetails.User(username, usuario.getChave(), authorities);
        }
        throw new UsernameNotFoundException("Não existe usuário com esse email ou cpf!");
    }
}
