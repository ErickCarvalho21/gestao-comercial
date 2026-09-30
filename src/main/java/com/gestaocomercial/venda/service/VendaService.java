package com.gestaocomercial.venda.service;

import com.gestaocomercial.estoque.service.EstoqueService;
import com.gestaocomercial.exception.*;
import com.gestaocomercial.produto.entity.Produto;
import com.gestaocomercial.produto.enums.TipoVendaEnums;
import com.gestaocomercial.produto.service.ProdutoService;
import com.gestaocomercial.venda.converter.ItemVendaConverter;
import com.gestaocomercial.venda.converter.VendaConverter;
import com.gestaocomercial.venda.dto.in.ItemVendaDTORequest;
import com.gestaocomercial.venda.dto.out.VendaDTOResponse;
import com.gestaocomercial.venda.entity.ItemVenda;
import com.gestaocomercial.venda.entity.Venda;
import com.gestaocomercial.venda.enums.StatusVendaEnums;
import com.gestaocomercial.venda.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.aot.generate.ValueCodeGenerationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;
    private final VendaConverter vendaConverter;
    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;

    public VendaDTOResponse abrirVenda(){

        Venda venda = Venda.builder().build();
        Venda vendaSalva = vendaRepository.save(venda);
        return vendaConverter.paraVendaDTOResponse(vendaSalva);

    }

    public Venda buscarVendaEntidadePorId(long id){
        return vendaRepository.findById(id).orElseThrow(()-> new VendaNaoEncontradaException(
                "Venda não encontrada" + id
        ));
    }



    void validarQuantidadeItem(Produto produtoEncontrado, BigDecimal quantidadeIten){

        if(produtoEncontrado.getTipoVendaEnums().equals(TipoVendaEnums.UNIDADE)){
            if(quantidadeIten.stripTrailingZeros().scale() > 0){
                throw  new QuantidadeItemVendaInvalidaException(
                        "produto vendido por unidade não aceita quantidade fracionária"
                );
            }
        }

    }


    @Transactional
    public VendaDTOResponse adicionarItem(Long id, ItemVendaDTORequest request){

        Venda vendaEncontrada = buscarVendaEntidadePorId(id);

        if(vendaEncontrada.getStatusVendasEnums() != StatusVendaEnums.ABERTA){
            throw new StatusVendaInvalidoException(
                    "A venda não está aberta"
            );
        }

        Produto produtoEncontrado = produtoService.buscaProdutoEntidadeCodigoBarras(request.getCodigoBarras());


        if (!produtoEncontrado.isAtivo()){
            throw new ProdutoInativoException(
                    "Este produto esta inativo"
            );
        }

        estoqueService.validarEstoqueDisponivel(produtoEncontrado, request.getQuantidadeItem());

        validarQuantidadeItem(produtoEncontrado, request.getQuantidadeItem());

        BigDecimal subTotal =request.getQuantidadeItem().multiply(produtoEncontrado.getPrecoVenda());


        ItemVenda itemVenda = ItemVenda.builder()
                .quantidadeItem(request.getQuantidadeItem())
                .venda(vendaEncontrada)
                .produto(produtoEncontrado)
                .precoItemVenda(produtoEncontrado.getPrecoVenda())
                .totalItemVenda(subTotal)
                .build();


        vendaEncontrada.getItens().add(itemVenda);

        vendaEncontrada.setValorTotal(vendaEncontrada.getValorTotal().add(subTotal));


        Venda salvarVenda = vendaRepository.save(vendaEncontrada);
        return vendaConverter.paraVendaDTOResponse(salvarVenda);

    }





}
