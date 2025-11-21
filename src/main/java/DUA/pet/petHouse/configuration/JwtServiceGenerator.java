package DUA.pet.petHouse.configuration;

import DUA.pet.petHouse.model.UsuarioModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Service
public class JwtServiceGenerator {

    // Parâmetros do token
    private static final String SECRET_KEY = "1bMms4S1CReqdr7H1vYO/CQ5aw98jEYJ4lJ3ZLhmjwA=";
    private static final SignatureAlgorithm ALGORITMO_ASSINATURA = SignatureAlgorithm.HS256;
    private static final int HORAS_EXPIRACAO_TOKEN = 1;

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

    public String generateToken(UsuarioModel usuario) {
        Map<String, Object> payloadData = this.gerarPayload(usuario);

        return Jwts.builder()
                .setClaims(payloadData)
                .setSubject(usuario.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600000L * HORAS_EXPIRACAO_TOKEN))
                .signWith(getSigningKey(), ALGORITMO_ASSINATURA)
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
}