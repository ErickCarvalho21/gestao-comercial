package com.gestaocomercial.produto.service;

import com.gestaocomercial.exception.CodigoBarrasDuplicationException;
import com.gestaocomercial.exception.ProdutoNaoEncontradoException;
import com.gestaocomercial.produto.Converter.ProdutoConverter;
import com.gestaocomercial.produto.dto.in.ProdutoDTORequest;
import com.gestaocomercial.produto.dto.out.ProdutoDTOResponse;
import com.gestaocomercial.produto.entity.Produto;
import com.gestaocomercial.produto.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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

    public List<ProdutoDTOResponse> listarProdutos(String nome){
        List<Produto> produtosEncontrados;

        if(nome == null|| nome.isBlank()){
            produtosEncontrados = produtoRepository.findAll();
        } else{
            produtosEncontrados = produtoRepository.findByNomeContainingIgnoreCase(nome);
        }
        return produtosEncontrados.stream()
                .map(produto -> produtoConverter.paraProdutoDTOResponse(produto))
                .toList();
    }

    public ProdutoDTOResponse buscarProdutoCodigoBarra(String codigoBarras){
           Produto produtoEncontrado =
                produtoRepository.findByCodigoBarras(codigoBarras)
                        .orElseThrow(()->new ProdutoNaoEncontradoException(
                                " Produto nao encontrado: " + codigoBarras
                        ));

           return produtoConverter.paraProdutoDTOResponse(produtoEncontrado);
    }
    public Produto verificarProdutoExistenteId(Long id) {
        return produtoRepository.findById(id).orElseThrow(() -> new ProdutoNaoEncontradoException(
                "Produto nao encontrado: " + id
        ));
    }
    private void validarCodigoBarrasDuplicado(String codigoBarras, long id){
        boolean codigoDuplicado =
                produtoRepository.existsByCodigoBarrasAndIdNot(codigoBarras, id);

        if(codigoDuplicado){
            throw new CodigoBarrasDuplicationException(
                    "Este codigo de brras ja existe"
            );
        }
    }

    public ProdutoDTOResponse buscarProdutoPorId(Long id){
        Produto produto = buscarProdutoEntidadePorId(id);
        return produtoConverter.paraProdutoDTOResponse(produto);
    }

    private Produto buscarProdutoEntidadePorId(Long id){
        return produtoRepository.findById(id).orElseThrow(()-> new ProdutoNaoEncontradoException(
                        "Produto não Encontrado: " + id
                ));
    }

    @Transactional
    public ProdutoDTOResponse atualizarProduto(long id, ProdutoDTORequest produtoDTORequest){

        Produto produto = buscarProdutoEntidadePorId(id);
        if(!produtoDTORequest.getCodigoBarras().equals(produto.getCodigoBarras())){
            validarCodigoBarrasDuplicado(produtoDTORequest.getCodigoBarras(), id);
        }
        Produto produtoAtualizado = produtoConverter.updateProduto(produtoDTORequest, produto);
        return produtoConverter.paraProdutoDTOResponse(produtoRepository.save(produtoAtualizado));

    }

}
