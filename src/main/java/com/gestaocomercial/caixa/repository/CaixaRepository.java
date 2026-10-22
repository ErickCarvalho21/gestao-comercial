package com.gestaocomercial.caixa.repository;

import com.gestaocomercial.caixa.entity.Caixa;

import com.gestaocomercial.caixa.enums.StatusCaixaEnums;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CaixaRepository extends JpaRepository<Caixa, Long> {

    Boolean existsByStatusCaixaEnums(StatusCaixaEnums status);
}
