package DUA.petHouse.repository;

import DUA.petHouse.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    Optional<UsuarioModel> findByEmailOrCpf(String email, String cpf);

    Optional<UsuarioModel> findByUser(String login);


}
