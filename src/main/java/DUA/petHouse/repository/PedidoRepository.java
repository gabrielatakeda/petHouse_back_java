package DUA.petHouse.repository;

import DUA.petHouse.model.PedidoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoModel, Long> {
    Optional<PedidoModel> findByClienteId(Long clienteId);
}
