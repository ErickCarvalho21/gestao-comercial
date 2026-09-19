package com.gestaocomercial.produto.repository;

import com.gestaocomercial.produto.entity.Produto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{

    Optional<Produto> findByCodigoBarras(String codigoBarras);

    Optional<Produto> findById(Long id);

    Boolean existsByCodigoBarras(String codigoBarras);

    boolean existsByCodigoBarrasAndIdNot(String codigoBrras, long id);

    List<Produto> findByNomeContainingIgnoreCase(String nome);

    @Query("""
    SELECT p
    FROM Produto p
    WHERE p.quantidadeEstoque <= p.estoqueMinimo
    """)
    List<Produto> buscarProdutoEstoqueBaixo();

}


