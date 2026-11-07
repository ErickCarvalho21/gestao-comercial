package com.gestaocomercial.relatorio.dto.out;


import lombok.*;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProdutoMaisVendidoDTOResponse {

    private Long produtoId;

    private String nomeProduto;

    private BigDecimal quantidadeVendida;
}
