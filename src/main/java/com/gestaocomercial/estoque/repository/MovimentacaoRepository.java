package com.gestaocomercial.estoque.repository;

import com.gestaocomercial.estoque.entity.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentacaoRepository extends JpaRepository<MovimentacaoEstoque, Long> {
}
