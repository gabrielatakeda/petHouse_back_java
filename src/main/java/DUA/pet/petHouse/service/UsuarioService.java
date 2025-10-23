package DUA.pet.petHouse.service;

import DUA.pet.petHouse.model.UsuarioModel;
import DUA.pet.petHouse.repository.UsuarioRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public UsuarioModel save(UsuarioModel usuarioModel){
        usuarioModel.setSenha(passwordEncoder.encode(usuarioModel.getSenha()));
        return usuarioRepository.save(usuarioModel);
    }

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
        if (usuarioUpdate.getSenha() != usuarioModel.getSenha()) {
            usuarioUpdate.setSenha(passwordEncoder.encode(usuarioModel.getSenha()));
        } else if (usuarioUpdate.getSenha() == usuarioModel.getSenha()) {
            usuarioUpdate.setSenha(usuarioModel.getSenha());
        }
        return usuarioRepository.save(usuarioUpdate);
    }

    public List<UsuarioModel> findAll(){
        return usuarioRepository.findAll();
    }

    public UsuarioModel login(String usuarioLogin, String senha) {
        UsuarioModel usuario = usuarioRepository.findByEmailOrCpf(usuarioLogin, usuarioLogin)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new RuntimeException("Senha incorreta");
        }

        return usuario;
    }


}
