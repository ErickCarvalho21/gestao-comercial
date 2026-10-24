package com.gestaocomercial.caixa.repository;

import com.gestaocomercial.caixa.entity.Caixa;

import com.gestaocomercial.caixa.enums.StatusCaixaEnums;
import com.gestaocomercial.produto.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CaixaRepository extends JpaRepository<Caixa, Long> {


    Optional<Caixa> findById(Long id);

    Optional<Caixa> findByStatusCaixaEnums(StatusCaixaEnums status);

    Boolean existsByStatusCaixaEnums(StatusCaixaEnums status);
}
