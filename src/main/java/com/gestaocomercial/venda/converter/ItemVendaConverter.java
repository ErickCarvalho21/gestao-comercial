package com.gestaocomercial.venda.converter;

import com.gestaocomercial.venda.dto.in.ItemVendaDTORequest;
import com.gestaocomercial.venda.dto.out.ItemVendaDTOResponse;
import com.gestaocomercial.venda.entity.ItemVenda;
import org.springframework.stereotype.Component;

@Component
public class ItemVendaConverter {

    public ItemVenda paraItemVenda(ItemVendaDTORequest itemVendaDTO){
        return ItemVenda.builder()

                .quantidadeItem(itemVendaDTO.getQuantidadeItem())
                .build();

    }

    public ItemVendaDTOResponse paraItemVendaDTOResponse(ItemVenda itemVenda){
        return ItemVendaDTOResponse.builder()
                .produtoId(itemVenda.getProduto().getId())
                .precoItemVenda(itemVenda.getPrecoItemVenda())
                .totalItemVenda(itemVenda.getTotalItemVenda())
                .quantidadeItem(itemVenda.getQuantidadeItem())
                .id(itemVenda.getId())
                .build();
    }
}
