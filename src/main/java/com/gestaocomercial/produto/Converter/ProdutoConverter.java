package com.gestaocomercial.produto.Converter;

import com.gestaocomercial.produto.dto.in.ProdutoAtualizacaoDTORequest;
import com.gestaocomercial.produto.dto.in.ProdutoDTORequest;
import com.gestaocomercial.produto.dto.out.ProdutoDTOResponse;
import com.gestaocomercial.produto.entity.Produto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProdutoConverter {

    public Produto paraProduto(ProdutoDTORequest produtodto){
        return Produto.builder()
                .nome(produtodto.getNome())
                .codigoBarras(produtodto.getCodigoBarras())
                .descricao(produtodto.getDescricao())
                .precoVenda(produtodto.getPrecoVenda())
                .tipoVendaEnums(produtodto.getTipoVendaEnums())
                .precoCompra(produtodto.getPrecoCompra())
                .quantidadeEstoque(BigDecimal.ZERO)
                .estoqueMinimo(produtodto.getEstoqueMinimo())
                .build();
    }
    
    public ProdutoDTOResponse paraProdutoDTOResponse(Produto produto){
        return ProdutoDTOResponse.builder()
                .nome(produto.getNome())
                .codigoBarras(produto.getCodigoBarras())
                .descricao(produto.getDescricao())
                .precoVenda(produto.getPrecoVenda())
                .tipoVendaEnums(produto.getTipoVendaEnums())
                .precoCompra(produto.getPrecoCompra())
                .quantidadeEstoque(produto.getQuantidadeEstoque())
                .estoqueMinimo(produto.getEstoqueMinimo())
                .id(produto.getId())
                .dataCadastro(produto.getDataCadastro())
                .isAtivo(produto.isAtivo())
                .build();
    }

    public Produto updateProduto (ProdutoAtualizacaoDTORequest produtoAtualizacaoDTORequest, Produto entity){
                entity.setNome(produtoAtualizacaoDTORequest.getNome());
                entity.setDescricao(produtoAtualizacaoDTORequest.getDescricao());
                entity.setCodigoBarras(produtoAtualizacaoDTORequest.getCodigoBarras());
                entity.setPrecoVenda(produtoAtualizacaoDTORequest.getPrecoVenda());
                entity.setTipoVendaEnums(produtoAtualizacaoDTORequest.getTipoVendaEnums());
                entity.setPrecoCompra(produtoAtualizacaoDTORequest.getPrecoCompra());
                entity.setEstoqueMinimo(produtoAtualizacaoDTORequest.getEstoqueMinimo());
                return entity;
    }




}
