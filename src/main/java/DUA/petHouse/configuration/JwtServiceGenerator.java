package DUA.petHouse.configuration;

import DUA.petHouse.model.UsuarioModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceGenerator {

    private final JwtProperties jwtProperties;

    @Autowired
    public JwtServiceGenerator(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties; // guarda o bean com as configs
    }

    // define o algoritmo utilizado para assinar o token (HMAC-SHA256)
    private static final SignatureAlgorithm ALGORITMO_ASSINATURA = SignatureAlgorithm.HS256;

    // cria o payload (claims) personalizados a partir do UsuarioModel
    public Map<String, Object> gerarPayload(UsuarioModel usuario) {
        Map<String, Object> payloadData = new HashMap<>();
        payloadData.put("username", usuario.getUser());
        payloadData.put("nome", usuario.getNome());
        payloadData.put("email", usuario.getEmail());
        payloadData.put("cpf", usuario.getCpf());
        payloadData.put("id", usuario.getId().toString());
        payloadData.put("role", usuario.getRole());
        payloadData.put("endereco", usuario.getEnderecos());
        return payloadData;
    }

    // gera o token JWT usando os claims e as configurações
    public String generateToken(UsuarioModel usuario) {
        Map<String, Object> payloadData = this.gerarPayload(usuario); // monta os claims

        long expirationMillis = 3600000L * jwtProperties.getExpirationHours();
        // calcula expiração em milissegundos

        return Jwts.builder() // inicia o builder do JJWT
                .setClaims(payloadData) // define os claims personalizados
                .setSubject(usuario.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis())) // data de emissão
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis)) // define expiração
                .signWith(getSigningKey(), ALGORITMO_ASSINATURA)
                .compact(); // compacta e retorna a string do token
    }

    // cria a Key de assinatura a partir da secret
    private Key getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        // cria uma Key apropriada para o JWT usando os bytes
        return Keys.hmacShaKeyFor(keyBytes);

    }

    // extrai todos os claims do token usando a mesma chave de assinatura
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token) // verifica assinatura
                .getBody();
    }

    // extrai o username (subject) do token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // metodo para extrair qualquer claim aplicando uma função sobre Claims
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // extrai a data de expiração do token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // verifica se o token já expirou comparando a data de expiração com agora
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // valida se o token pertence ao userDetails fornecido e não expirou
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // pega o subject do token
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

}
