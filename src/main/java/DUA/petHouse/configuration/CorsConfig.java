package DUA.petHouse.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig{
    //classe para configuração de requisições do front para o back (no caso, esta totalmente liberada)

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");// permite requisições vindas de qualquer origem (domínio)
        config.addAllowedHeader("*");// permite que o cliente envie qualquer header nas requisições
        config.addAllowedMethod("*");// permite uso de qualquer metodo HTTP

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // aplica esta configuração CORS para todas as rotas da API
        return source;
    }
}
