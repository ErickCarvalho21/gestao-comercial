package com.gestaocomercial.venda.dto.out;

import com.gestaocomercial.venda.enums.FormaPagamentoEnums;
import com.gestaocomercial.venda.enums.StatusVendaEnums;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class VendaDTOResponse {

    private Long id;
    private StatusVendaEnums statusVendasEnums;
    private BigDecimal valorTotal;
    private FormaPagamentoEnums formaPagamentosEnums;
    private LocalDateTime dataVenda;
    private List<ItemVendaDTOResponse> itens;

}
