package DUA.petHouse.config;

import DUA.petHouse.model.UsuarioModel;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceGenerator {

    public String generateToken(UsuarioModel usuarioModel){

        //Aqui é configurado o que vai no payload do token, tudo o que for colocado aqui, aparece no payload
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", usuarioModel.getId().toString());
        extraClaims.put("email", usuarioModel.getEmail());
        extraClaims.put("role", usuarioModel.getRole()); //Mandando a permissão, que é o tipo de acesso que ele possui no sistema
        extraClaims.put("nome", usuarioModel.getNome()); //Mandando o nome do usuário que está logando

//        return Jwts
//                .builder()
//                .setClaims(extraClaims)
//                .setSubject(usuarioModel.getUser())
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(new Date().getTime() + 3600000 * JwtConfig.HORAS_EXPIRACAO_TOKEN))
//                .signWith(getSigningKey(), JwtConfig.ALGORITMO_ASSINATURA)
    }
}