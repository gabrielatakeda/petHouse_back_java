package DUA.pet.petHouse.repository;

import DUA.pet.petHouse.model.CategoriaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<CategoriaModel, Long> {

    Optional<CategoriaModel> findByNome(String nome);
    Optional<CategoriaModel> findBySlug(String slug);


}
