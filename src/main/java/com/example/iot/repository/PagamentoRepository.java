package com.example.iot.repository;

import com.example.iot.model.PagamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<PagamentoModel, Long> {

    /* tira esse comentario quando juntar com o PedidoModel
    List<PagamentoModel> findByPedidoId(Long pedidoId);
    */

    Optional<PagamentoModel> findByTransacaoId(String transacaoId);

}