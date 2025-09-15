package DUA.pet.com.repository;

import DUA.pet.com.model.CategoriaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<CategoriaModel, Long> {

    Optional<CategoriaModel> findByNome(String nome);


}
