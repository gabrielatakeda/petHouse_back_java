package com.example.iot.repository;

import com.example.iot.model.CategoriaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<CategoriaModel, Long> {

    Optional<CategoriaModel> findByNome(String nome);


}
