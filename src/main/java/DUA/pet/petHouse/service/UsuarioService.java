package DUA.pet.petHouse.service;

import DUA.pet.petHouse.model.UsuarioModel;
import DUA.pet.petHouse.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioModel save(UsuarioModel usuarioModel){
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
        usuarioUpdate.setSenha(usuarioModel.getSenha());
        usuarioUpdate.setUsuario(usuarioModel.getUsuario());
//        usuarioUpdate.setEnderecos(usuarioModel.getEnderecos());


        return usuarioRepository.save(usuarioUpdate);
    }

    public List<UsuarioModel> findAll(){
        return usuarioRepository.findAll();
    }
}
