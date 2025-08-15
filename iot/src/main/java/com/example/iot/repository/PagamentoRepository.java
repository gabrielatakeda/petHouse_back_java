package com.example.iot.repository;

import com.example.iot.model.PagamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<PagamentoModel, Long> {

    List<PagamentoModel> findByPedidoId(Long pedidoId);
    Optional<PagamentoModel> findByTransacaoId(String transacaoId);

}