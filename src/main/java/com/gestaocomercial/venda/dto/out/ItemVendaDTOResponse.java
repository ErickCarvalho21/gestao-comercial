package com.gestaocomercial.venda.dto.out;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ItemVendaDTOResponse {

    private Long id;
    private BigDecimal quantidadeItem;
    private BigDecimal precoItemVenda;
    private BigDecimal totalItemVenda;
    private Long produtoId;

}
