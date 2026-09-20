package com.gestaocomercial.estoque.repository;

import com.gestaocomercial.estoque.entity.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimentacaoRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    List<MovimentacaoEstoque> findAllByOrderByDataDescIdDesc();

    List<MovimentacaoEstoque> findByProdutoIdOrderByDataDescIdDesc(Long produtoId);


}
