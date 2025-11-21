package DUA.pet.petHouse.service;

import DUA.pet.petHouse.configuration.JwtServiceGenerator;
import DUA.pet.petHouse.model.UsuarioModel;
import DUA.pet.petHouse.repository.UsuarioRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    @Autowired
    JwtServiceGenerator jwtServiceGenerator;

    private final UsuarioRepository usuarioRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public UsuarioModel save(UsuarioModel usuarioModel){
        usuarioModel.setSenha(passwordEncoder.encode(usuarioModel.getSenha()));
        return usuarioRepository.save(usuarioModel);
    }

    @PreAuthorize("hasRole(USER)")
    public UsuarioModel findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    public void delete(Long id){
        usuarioRepository.deleteById(id);
    }

    public UsuarioModel update(Long id, @Valid UsuarioModel usuarioModel) {
        UsuarioModel usuarioUpdate = this.findById(id);
        usuarioUpdate.setEmail(usuarioModel.getEmail());
        usuarioUpdate.setNome(usuarioModel.getNome());
        usuarioUpdate.setUser(usuarioModel.getUser());
        usuarioUpdate.setCpf(usuarioModel.getCpf());
        usuarioUpdate.setDataNascimento(usuarioModel.getDataNascimento());

//      verifica se a senha do usuario foi alterada, caso contrario, o sitema faria uma criptografia
//      em cima de uma senha ja criptografada
        if (!passwordEncoder.matches(usuarioModel.getSenha(), usuarioUpdate.getSenha())) {
            usuarioUpdate.setSenha(passwordEncoder.encode(usuarioModel.getSenha()));
        }

        return usuarioRepository.save(usuarioUpdate);
    }

    public List<UsuarioModel> findAll(){
        return usuarioRepository.findAll();
    }

    public String login(String usuarioLogin, String senha) {

        System.out.println("LOGIN SERVICE: usuarioLogin=" + usuarioLogin);
        System.out.println("LOGIN SERVICE: senha=" + senha);

        UsuarioModel usuario = usuarioRepository.findByEmailOrCpf(usuarioLogin, usuarioLogin)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        System.out.println("USUARIO ENCONTRADO: " + usuario.getEmail());
        System.out.println("HASH NO BANCO: " + usuario.getSenha());
        System.out.println("SENHA DIGITADA: " + senha);
        System.out.println("MATCH? " + passwordEncoder.matches(senha, usuario.getSenha()));

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new RuntimeException("Senha incorreta");
        }

        String token = jwtServiceGenerator.generateToken(usuario);

        System.out.println("TOKEN GERADO (ANTES DE RETORNAR) = " + token);

        return token;
    }




}
