package com.gestaocomercial.estoque.converter;


import com.gestaocomercial.estoque.dto.in.MovimentacaoEstoqueDTORequest;
import com.gestaocomercial.estoque.dto.out.MovimentacaoEstoqueDTOResponse;
import com.gestaocomercial.estoque.entity.MovimentacaoEstoque;
import com.gestaocomercial.produto.entity.Produto;
import org.springframework.stereotype.Component;

@Component
public class EstoqueConverter {

    public MovimentacaoEstoque converterEstoque (MovimentacaoEstoqueDTORequest estoqueDTO, Produto produto){
        return MovimentacaoEstoque.builder()
                .produto(produto)
                .tipoMoviementacaoEnums(estoqueDTO.getTipoMovimentacaoEnums())
                .quantidade(estoqueDTO.getQuantidade())
                .build();
    }

    public MovimentacaoEstoqueDTOResponse paraEstoqueResponseDTO(MovimentacaoEstoque movimentacaoEstoque){
        return MovimentacaoEstoqueDTOResponse.builder()
                .produtoId(movimentacaoEstoque.getProduto().getId())
                .tipoMoviementacaoEnums(movimentacaoEstoque.getTipoMoviementacaoEnums())
                .quantidade(movimentacaoEstoque.getQuantidade())
                .id(movimentacaoEstoque.getId())
                .data(movimentacaoEstoque.getData())
                .build();
    }


}
