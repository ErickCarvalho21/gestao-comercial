package com.gestaocomercial.estoque.service;


import com.gestaocomercial.estoque.converter.EstoqueConverter;
import com.gestaocomercial.estoque.dto.in.MovimentacaoEstoqueDTORequest;
import com.gestaocomercial.estoque.dto.out.MovimentacaoEstoqueDTOResponse;
import com.gestaocomercial.estoque.entity.MovimentacaoEstoque;
import com.gestaocomercial.estoque.enums.TipoMovimentacaoEnums;
import com.gestaocomercial.estoque.repository.MovimentacaoRepository;
import com.gestaocomercial.exception.EstoqueInsuficienteException;
import com.gestaocomercial.exception.QuantidadeMovimentacaoInvalidaException;
import com.gestaocomercial.produto.dto.in.ProdutoDTORequest;
import com.gestaocomercial.produto.entity.Produto;
import com.gestaocomercial.produto.service.ProdutoService;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor

public class EstoqueService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final EstoqueConverter estoqueConverter;
    private final ProdutoService produtoService;

    private BigDecimal calcularNovoEstoque(Produto produto,
                                           MovimentacaoEstoqueDTORequest movimentacaoEstoqueDTORequest) {

        BigDecimal estoqueAtual = produto.getQuantidadeEstoque();
        BigDecimal quantidade = movimentacaoEstoqueDTORequest.getQuantidade();
        TipoMovimentacaoEnums tipo = movimentacaoEstoqueDTORequest.getTipoMovimentacaoEnums();

        if(quantidade.compareTo(BigDecimal.ZERO) < 0){
            throw new
                    QuantidadeMovimentacaoInvalidaException(
                    "Quantidade não pode ser negativa"
            );
        }

        if(quantidade.compareTo(BigDecimal.ZERO) == 0 && tipo != TipoMovimentacaoEnums.AJUSTE){
            throw new
                    QuantidadeMovimentacaoInvalidaException(
                    "Quantidade deve ser maior que zero para entrada ou saída"
            );
        }

        switch (tipo) {
            case ENTRADA: {
                return estoqueAtual.add(quantidade);
            }
            case SAIDA: {
                if (quantidade.compareTo(estoqueAtual) > 0) {
                    throw new EstoqueInsuficienteException(
                            "Produto insuficiente no estoque"
                    );
                }
                return estoqueAtual.subtract(quantidade);

            }
            case AJUSTE: {
                return quantidade;
            }

            default:
                throw  new IllegalArgumentException("Tipo de movimento invalido");
        }

    }

    @Transactional
    public MovimentacaoEstoqueDTOResponse registrarMovimentacao(MovimentacaoEstoqueDTORequest
                                                                       request){
        Produto produto = produtoService.buscarProdutoEntidadePorId(request.getProdutoId());

        BigDecimal novoEstoque = calcularNovoEstoque(produto, request);

        produto.setQuantidadeEstoque(novoEstoque);

        MovimentacaoEstoque movimentacaoEstoque = estoqueConverter.converterEstoque(request,
                produto);

        MovimentacaoEstoque movimentacaoSalva = movimentacaoRepository.save(movimentacaoEstoque);

        return estoqueConverter.paraEstoqueResponseDTO(movimentacaoSalva);

    }

    public List<MovimentacaoEstoqueDTOResponse> listarMovimentacoes(){
        List<MovimentacaoEstoque> movimetacoes = movimentacaoRepository.findAllByOrderByDataDescIdDesc();
        return movimetacoes.stream()
                .map(movimentacaoEstoque->
                        estoqueConverter.paraEstoqueResponseDTO(movimentacaoEstoque))
                .toList();
    }
    public List<MovimentacaoEstoqueDTOResponse> listarMovimentacaoPorProduto(Long produtoId){

        produtoService.buscarProdutoEntidadePorId(produtoId);
        List<MovimentacaoEstoque> movimentacoes = movimentacaoRepository.findByProdutoIdOrderByDataDescIdDesc(produtoId);

        return movimentacoes.stream().map(movimentacaoEstoque->
                estoqueConverter.paraEstoqueResponseDTO(movimentacaoEstoque))
                .toList();

    }




}
