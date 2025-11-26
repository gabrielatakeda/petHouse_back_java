package DUA.petHouse.auth;

import DUA.petHouse.model.UsuarioModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private  JwtService jwtService;

    public String logar(Login login) throws AuthenticationException{
        //Valida o email + senha no Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.getUsername(),
                        login.getSenha()
                )
        );

        //Busca usuário no banco de dados
        UsuarioModel usuarioModel = loginRepository.findByEmail(login.getUsername())
                .orElseThrow(() -> new AuthenticationException("Usuário não existe"));

        //Gera o token JWT
        return jwtService.generateToken(usuarioModel);
    }
}