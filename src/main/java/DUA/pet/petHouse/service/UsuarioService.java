package DUA.pet.petHouse.service;

import DUA.pet.petHouse.model.UsuarioModel;
import DUA.pet.petHouse.repository.UsuarioRepository;
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

        usuarioUpdate.setSenha(passwordEncoder.encode(usuarioModel.getSenha()));
        usuarioUpdate.setEmail(usuarioModel.getEmail());
        usuarioUpdate.setNome(usuarioModel.getNome());
        usuarioUpdate.setSenha(usuarioModel.getSenha());
        usuarioUpdate.setUser(usuarioModel.getUser());
//        usuarioUpdate.setEnderecos(usuarioModel.getEnderecos());


        return usuarioRepository.save(usuarioUpdate);
    }

    public List<UsuarioModel> findAll(){
        return usuarioRepository.findAll();
    }

    public UsuarioModel login(String usuarioLogin, String senha) {
        UsuarioModel usuario = usuarioRepository.findByEmailOrCpf(usuarioLogin, usuarioLogin)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new RuntimeException("Senha incorreta");
        }

        return usuario;
    }


}
