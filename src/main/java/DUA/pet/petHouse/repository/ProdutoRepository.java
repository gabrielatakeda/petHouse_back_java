package DUA.pet.petHouse.repository;

import DUA.pet.petHouse.model.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<ProdutoModel, Long> {
    Optional<ProdutoModel> findByNome(String nome);

    @Query(
            "Select p from ProdutoModel p where p.nome = :nome"
    )
    List<ProdutoModel> getByNome(@Param("nome") String nome);
}
