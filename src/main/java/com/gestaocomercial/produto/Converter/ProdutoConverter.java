package com.gestaocomercial.produto.Converter;

import com.gestaocomercial.exception.dto.ErrorDTOResponse;
import com.gestaocomercial.produto.dto.in.ProdutoDTORequest;
import com.gestaocomercial.produto.dto.out.ProdutoDTOResponse;
import com.gestaocomercial.produto.entity.Produto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


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
                .quantidadeEstoque(produtodto.getQuantidadeEstoque())
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
                .ativo(produto.isAtivo())
                .build();
    }



}
