package DUA.petHouse.auth;

import DUA.petHouse.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginRepository extends JpaRepository<UsuarioModel, Long> {

    public Optional<UsuarioModel> findByEmail(String email);
}
