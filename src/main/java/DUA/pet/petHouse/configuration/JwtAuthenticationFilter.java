package DUA.pet.petHouse.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtServiceGenerator jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(

            // Requisição HTTP recebida
            @NonNull HttpServletRequest request,
            // Resposta HTTP a ser devolvida
            @NonNull HttpServletResponse response,
            // Corrente de filtros que o Spring irá executar
            @NonNull FilterChain filterChain

    ) throws ServletException, IOException {

        // pega valor mandado no cabeçalho
        final String authHeader = request.getHeader("Authorization");
        final String jwt; // token
        final String userEmail; //email do token

        //caso não haja cabeçalho, ignora o filtro
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }

        // pula o "Bearer" do token
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        //caso email extraido e ninguem esteja autenticado
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            //pega o usuario do banco
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // Verifica se o token é válido para este usuário
            if(jwtService.isTokenValid(jwt, userDetails)) {

                // Cria um objeto de autenticação para o Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()

                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);

            }

        }

        filterChain.doFilter(request, response);

    }

}
