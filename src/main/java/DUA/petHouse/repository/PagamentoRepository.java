package DUA.petHouse.repository;

import DUA.petHouse.model.PagamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends JpaRepository<PagamentoModel, Long> {

    /* tira esse comentario quando juntar com o PedidoModel
    List<PagamentoModel> findByPedidoId(Long pedidoId);
    */

    //Optional<PagamentoModel> findByTransacaoId(String transacaoId);

}