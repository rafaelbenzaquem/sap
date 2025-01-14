package br.jus.trf1.sap.security;

//import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

//@Configuration
//@EnableWebSecurity
public class WebSecurityConfiguration {

//    @Bean
//    public static PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
////                .csrf(c -> c.ignoringRequestMatchers(PathRequest.toH2Console())
////                .disable()
////                )
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/", "/home").permitAll()
////                        .requestMatchers(PathRequest.toH2Console()).permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(formLogin -> formLogin.loginPage("/login")
//                        .permitAll()
//                )
//                .logout((logout) -> logout.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()));
//
//        return httpSecurity.build();
//    }
}