package com.example.iot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //Classe se configuração do Spring, vai ler esse arquivo ao iniciar no servidor
@EnableWebSecurity //Ativa o Spring Security, sem isso, não aplica nenhuma regra de segurança
public class SecurityConfig {

    @Autowired //Injeta as dependências automaticamente
    private JwtAuthenticationFilter jwtAuthenticationFilter; //Filtro que ê o token e valida o usuário

    @Autowired
    private AuthenticationProvider authenticationProvider; //Autentica o usuário

    @Bean //Devolve a cadeia de filtros de segurança do Spring Security, executa esses filtros em toda requisição HTTP
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { //Objeto que é a base de toda configuração de segurança da API, o metodo pode lançar exceções

        return http //É um objeto do tipo HttpSecurity, retorna o objeto com todas as configurações aplicadas
                .csrf(AbstractHttpConfigurer::disable) //Tipo de ataque onde alguém tenta fazer o usuário enviar requisições sem querer, estamos usando o JWT
                .cors(AbstractHttpConfigurer::disable) //Define quais frontends podem ser acessados pela API
                .authorizeHttpRequests(requests -> requests //Aqui é quem pode acessar quais endpoisnt, define regras de permissão
                        .requestMatchers("/api/login").permitAll() //Essas rotas não precisam de login, são públicas
                        .requestMatchers("/api/register").permitAll()
                        .anyRequest().authenticated() //Todas as outras rotas exigem token válido ou retorna 401
                )
                .authenticationProvider(authenticationProvider) //Provider personalizado de autenticação
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) //Spring recebe uma requisição, se houver token ele valida, autentica o usuário e libera acesso
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //Configura política de sessão, STATELESS não guarda sessão, não usa cookie e a cada requisição deve ter seu próprio token
                .build(); //Finaliza toda a configuração de segurança e constrói o objeto da classe
    }
}