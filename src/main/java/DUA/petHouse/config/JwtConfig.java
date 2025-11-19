package DUA.petHouse.config;

import io.jsonwebtoken.SignatureAlgorithm;

public class JwtConfig { //Parâmetros para a geração do token

    /*Essencial que existe uma chave secreta que apenas o backend saiba, pode ser escrito qualquer coisa, ou seja,
    essa chave secreta é um hash qualquer. Serve para bagunçar o token junto com o algoritmo e gera o token.*/
    public static final SignatureAlgorithm ALGORITMO_ASSINATURA = SignatureAlgorithm.HS256; //Algoritmo criptografado para fazer o header, payload e signature virarem um token assinado
    public static final int HORAS_EXPIRACAO_TOKEN = 1; //Tempo de duração em horas, pode ser alterado para minutos
}
