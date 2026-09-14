package com.gestaocomercial.produto.service;

import com.gestaocomercial.exception.CodigoBarrasDuplicationException;
import com.gestaocomercial.produto.Converter.ProdutoConverter;
import com.gestaocomercial.produto.dto.in.ProdutoDTORequest;
import com.gestaocomercial.produto.dto.out.ProdutoDTOResponse;
import com.gestaocomercial.produto.entity.Produto;
import com.gestaocomercial.produto.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoConverter produtoConverter;

    public ProdutoDTOResponse cadastrarProduto(ProdutoDTORequest produtoDTORequest){

        if(produtoRepository.existsByCodigoBarras(produtoDTORequest.getCodigoBarras())){
            throw  new CodigoBarrasDuplicationException(
                    "Este Codigo de Barras ja esta cadrastado"
            );
        }
        Produto produto = produtoConverter.paraProduto(produtoDTORequest);

        //return produtoConverter.paraProdutoDTOResponse(
        // produtoRepository.save(produto));

        Produto salvarProduto = produtoRepository.save(produto);
        return produtoConverter.paraProdutoDTOResponse(salvarProduto);

    }

}
