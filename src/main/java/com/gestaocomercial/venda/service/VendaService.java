package com.gestaocomercial.venda.service;

import com.gestaocomercial.caixa.entity.Caixa;
import com.gestaocomercial.caixa.service.CaixaService;
import com.gestaocomercial.estoque.dto.in.MovimentacaoEstoqueDTORequest;
import com.gestaocomercial.estoque.enums.TipoMovimentacaoEnums;
import com.gestaocomercial.estoque.service.EstoqueService;
import com.gestaocomercial.exception.*;
import com.gestaocomercial.produto.entity.Produto;
import com.gestaocomercial.produto.enums.TipoVendaEnums;
import com.gestaocomercial.produto.service.ProdutoService;

import com.gestaocomercial.venda.converter.VendaConverter;
import com.gestaocomercial.venda.dto.in.AlterarQuantidadeItemDTORequest;
import com.gestaocomercial.venda.dto.in.ItemVendaDTORequest;
import com.gestaocomercial.venda.dto.in.PagamentoVendaDTORequest;
import com.gestaocomercial.venda.dto.out.VendaDTOResponse;
import com.gestaocomercial.venda.entity.ItemVenda;
import com.gestaocomercial.venda.entity.Venda;
import com.gestaocomercial.venda.enums.StatusVendaEnums;
import com.gestaocomercial.venda.repository.ItemVendaRepository;
import com.gestaocomercial.venda.repository.VendaRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ItemVendaRepository itemVendaRepository;
    private final VendaConverter vendaConverter;
    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;
    private final CaixaService caixaService;

    public VendaDTOResponse abrirVenda(){

        Caixa caixaEncontrado = caixaService.buscarCaixaAbertoEntidade();

        Venda venda = Venda.builder().build();
        venda.setCaixa(caixaEncontrado);
        Venda vendaSalva = vendaRepository.save(venda);
        return vendaConverter.paraVendaDTOResponse(vendaSalva);
    }

    public Venda buscarVendaEntidadePorId(long id){
        return vendaRepository.findById(id).orElseThrow(()-> new VendaNaoEncontradaException(
                "Venda não encontrada" + id
        ));
    }

    public ItemVenda buscarItemVendaPorId(Long itemId){
        return itemVendaRepository.findById(itemId).orElseThrow(()-> new ItenVendaNaoEncontradoException(
                "item venda não foi encontrado" + itemId
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
    private BigDecimal calcularQuantidadeAcumuladaSemItemAtual(Venda venda, Produto produto, ItemVenda itemAtual){

        BigDecimal quantidadeAcumulada = BigDecimal.ZERO;

        for (ItemVenda item : venda.getItens()) {
            if(item.getProduto().getId().equals(produto.getId()) && !item.getId().equals(itemAtual.getId())){
                quantidadeAcumulada = quantidadeAcumulada.add(item.getQuantidadeItem());

            }

        }

        return quantidadeAcumulada;

    }


    private BigDecimal calcularQuantidadeAcumulada(Venda venda, Produto produto){
        BigDecimal quantidadeAcumulada = BigDecimal.ZERO;

        for(ItemVenda item : venda.getItens()){
            if(item.getProduto().getId().equals(produto.getId())){
            quantidadeAcumulada = quantidadeAcumulada.add(item.getQuantidadeItem());
            }
        }
        return quantidadeAcumulada;
    }

    private void recalcularTotalVenda(Venda venda) {
        BigDecimal total = BigDecimal.ZERO;

        for (ItemVenda item : venda.getItens()) {
            total = total.add(item.getTotalItemVenda());
        }

        venda.setValorTotal(total);


    }


    @Transactional
    public VendaDTOResponse alterarQuantidade(Long vendaId, Long itemId, AlterarQuantidadeItemDTORequest request){

        Venda vendaEncontrada = buscarVendaEntidadePorId(vendaId);

        if(vendaEncontrada.getStatusVendasEnums() != StatusVendaEnums.ABERTA){
            throw new StatusVendaInvalidoException(
                    "A venda não está aberta"
            );
        }

        ItemVenda itemVendaEncontrada = buscarItemVendaPorId(itemId);



        if(!itemVendaEncontrada.getVenda().getId().equals(vendaEncontrada.getId())){

            throw  new ItemVendaNaoPertenceAVendaException(
                    "Esse item não pertence a essa venda "
            );
        }

        validarQuantidadeItem(itemVendaEncontrada.getProduto(), request.getQuantidadeItem());

        BigDecimal quantidadeOutrosItens =
                calcularQuantidadeAcumuladaSemItemAtual(
                        vendaEncontrada,
                        itemVendaEncontrada.getProduto(),
                        itemVendaEncontrada
                );

        BigDecimal quantidadeTotalPretendida = quantidadeOutrosItens.add(request.getQuantidadeItem());

        estoqueService.validarEstoqueDisponivel(itemVendaEncontrada.getProduto(), quantidadeTotalPretendida);

        BigDecimal novoSubtotal = request.getQuantidadeItem().multiply(itemVendaEncontrada.getPrecoItemVenda());



        itemVendaEncontrada.setQuantidadeItem(request.getQuantidadeItem());
        itemVendaEncontrada.setTotalItemVenda(novoSubtotal);

        recalcularTotalVenda(vendaEncontrada);

        Venda salvarVenda = vendaRepository.save(vendaEncontrada);

        return vendaConverter.paraVendaDTOResponse(salvarVenda);


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

        BigDecimal quantidadeAcumulada = calcularQuantidadeAcumulada(vendaEncontrada, produtoEncontrado);

        BigDecimal quantidadeTotal = quantidadeAcumulada.add(request.getQuantidadeItem());




        estoqueService.validarEstoqueDisponivel(produtoEncontrado, quantidadeTotal);



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

    @Transactional
    public VendaDTOResponse removerItem(Long vendaId, Long itemId){

        Venda vendaEncontrada = buscarVendaEntidadePorId(vendaId);

        if(vendaEncontrada.getStatusVendasEnums() != StatusVendaEnums.ABERTA){
            throw new StatusVendaInvalidoException(
                    "A venda não está aberta"
            );
        }
        ItemVenda itemVendaEncontrado = buscarItemVendaPorId(itemId);

        if(!itemVendaEncontrado.getVenda().getId().equals(vendaEncontrada.getId())){

            throw  new ItemVendaNaoPertenceAVendaException(
                    "Esse item não pertence a essa venda "
            );
        }

        vendaEncontrada.getItens().remove(itemVendaEncontrado);
        itemVendaRepository.delete(itemVendaEncontrado);

        recalcularTotalVenda(vendaEncontrada);

        Venda salvarVenda = vendaRepository.save(vendaEncontrada);

        return vendaConverter.paraVendaDTOResponse(salvarVenda);

    }


    @Transactional
    public VendaDTOResponse definirPagamento( Long vendaId, PagamentoVendaDTORequest request){

        Venda vendaEncontrada = buscarVendaEntidadePorId(vendaId);

        if(vendaEncontrada.getStatusVendasEnums() != StatusVendaEnums.ABERTA){
            throw new StatusVendaInvalidoException(
                    "Venda nao esta aberta"
            );
        }

        if(vendaEncontrada.getItens().isEmpty()){
            throw new VendaSemItensException(
                    "A venda está vazia"
            );
        }

        vendaEncontrada.setFormaPagamentosEnums(request.getFormaPagamentoEnums());
        Venda vendaSalva = vendaRepository.save(vendaEncontrada);

        return vendaConverter.paraVendaDTOResponse(vendaSalva);


    }

    @Transactional
    public VendaDTOResponse finalizarVenda (Long vendaId){

        Venda vendaEncontrada = buscarVendaEntidadePorId(vendaId);

        if(vendaEncontrada.getStatusVendasEnums() != StatusVendaEnums.ABERTA){
            throw new StatusVendaInvalidoException(
                    "Venda nao esta aberta"
            );
        }

        if(vendaEncontrada.getItens().isEmpty()){
            throw new VendaSemItensException(
                    "A venda está vazia"
            );
        }

        if (vendaEncontrada.getFormaPagamentosEnums() == null) {
            throw new FormaPagamentoNaoDefinidaException(
                    "A forma de pagamento não foi definida"
            );
        }

        for (ItemVenda item : vendaEncontrada.getItens()) {

            estoqueService.validarEstoqueDisponivel(
                    item.getProduto(),
                    item.getQuantidadeItem()
            );
        }




        for (ItemVenda item : vendaEncontrada.getItens()) {
            MovimentacaoEstoqueDTORequest movimentacaoRequest =
                    MovimentacaoEstoqueDTORequest.builder()
                            .produtoId(item.getProduto().getId())
                            .quantidade(item.getQuantidadeItem())
                            .tipoMovimentacaoEnums(TipoMovimentacaoEnums.SAIDA)
                            .build();

            estoqueService.registrarMovimentacao(movimentacaoRequest);
        }

        vendaEncontrada.setStatusVendasEnums(
                StatusVendaEnums.FINALIZADA
        );

        Venda vendaSalva = vendaRepository.save(vendaEncontrada);

        return vendaConverter.paraVendaDTOResponse(vendaSalva);
    }


    @Transactional
    public VendaDTOResponse cancelarVenda (Long vendaId){

        Venda vendaEncontrada = buscarVendaEntidadePorId(vendaId);


        if (vendaEncontrada.getStatusVendasEnums() != StatusVendaEnums.ABERTA ){

            throw new StatusVendaInvalidoException(
                    "A venda precisa estar aberta para ser cancelada"
            );

        }

        vendaEncontrada.setStatusVendasEnums(StatusVendaEnums.CANCELADA);

        Venda salvarVenda = vendaRepository.save(vendaEncontrada);

        return vendaConverter.paraVendaDTOResponse(salvarVenda);
    }




}




