package DUA.petHouse.repository;

import DUA.petHouse.model.CategoriaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<CategoriaModel, Long> {

    Optional<CategoriaModel> findByNome(String nome);
    Optional<CategoriaModel> findBySlug(String slug);
    // Repository
    @Query("SELECT c FROM CategoriaModel c WHERE c.categoriaPai IS NULL")
    List<CategoriaModel> findCategoriasPai();


}
