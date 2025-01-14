package br.jus.trf1.sap.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UsuarioDetailsService { //implements UserDetailsService {
//
//    @Autowired
//    private UsuarioRepository usuarioRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        var usuarioOptional = usuarioRepository.findUsuarioByUsername(username);
//        if (usuarioOptional.isPresent()) {
//            var usuario = usuarioOptional.get();
//            var perfil = usuario.getPerfil();
//            return switch (perfil) {
//                case ADMINISTRADOR -> new User(username, usuario.getPassword(), Stream.of("ADMINISTRADOR")
//                        .map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
//                case DIRETOR -> new User(username, usuario.getPassword(), Stream.of("DIRETOR", "SERVIDOR")
//                        .map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
//                case SUPERVISOR -> new User(username, usuario.getPassword(), Stream.of("SUPERVISOR", "SERVIDOR")
//                        .map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
//                case SERVIDOR -> new User(username, usuario.getPassword(), Stream.of("SERVIDOR")
//                        .map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
//                case PRESTADOR -> new User(username, usuario.getPassword(), Stream.of("PRESTADOR")
//                        .map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
//                case ESTAGIARIO -> new User(username, usuario.getPassword(), Stream.of("ESTAGIARIO")
//                        .map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
//            };
//
//        }
//        throw new UsernameNotFoundException("Matrícula ou senha incorreta!");
//    }
}
