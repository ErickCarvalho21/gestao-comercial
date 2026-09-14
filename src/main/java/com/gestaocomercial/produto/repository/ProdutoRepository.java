package com.gestaocomercial.produto.repository;

import com.gestaocomercial.produto.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ProdutoRepository extends JpaRepository<Produto, Long>{

    Optional<Produto> findByCodigoBarras(String codigoBarras);

    Boolean existsByCodigoBarras(String codigoBarras);
}


