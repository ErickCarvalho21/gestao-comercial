package com.gestaocomercial.venda.converter;

import com.gestaocomercial.venda.dto.out.ItemVendaDTOResponse;
import com.gestaocomercial.venda.dto.out.VendaDTOResponse;
import com.gestaocomercial.venda.entity.Venda;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VendaConverter {
    private final ItemVendaConverter itemVendaConverter;





    public VendaDTOResponse paraVendaDTOResponse(Venda venda){

        List<ItemVendaDTOResponse> itensDTO = venda.getItens()
                .stream()
                .map(itemVendaConverter::paraItemVendaDTOResponse).toList();

        return VendaDTOResponse.builder()
                .id(venda.getId())
                .statusVendasEnums(venda.getStatusVendasEnums())
                .formaPagamentosEnums(venda.getFormaPagamentosEnums())
                .valorTotal(venda.getValorTotal())
                .dataVenda(venda.getDataVenda())
                .itens(itensDTO)
                .build();
    }

}
