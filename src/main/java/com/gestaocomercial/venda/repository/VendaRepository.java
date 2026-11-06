package com.gestaocomercial.venda.repository;

import com.gestaocomercial.venda.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    @Override
    Optional<Venda> findById(Long id);

    List<Venda> findByDataVendaBetween(
            LocalDateTime inicio,
            LocalDateTime fim
    );

}
