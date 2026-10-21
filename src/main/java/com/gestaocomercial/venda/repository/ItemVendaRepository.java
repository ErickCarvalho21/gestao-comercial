package com.gestaocomercial.venda.repository;

import com.gestaocomercial.venda.entity.ItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {

    @Override
    Optional<ItemVenda> findById(Long itemId);


}


