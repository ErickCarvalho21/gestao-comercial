package com.gestaocomercial.venda.repository;

import com.gestaocomercial.venda.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<Venda, Long> {
}
