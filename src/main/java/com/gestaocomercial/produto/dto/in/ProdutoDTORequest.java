package com.gestaocomercial.produto.dto.in;


import com.gestaocomercial.produto.enums.TipoVendaEnums;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProdutoDTORequest {


    @NotBlank
    private String nome;
    @NotBlank
    private String codigoBarras;

    private String descricao;

    @NotNull
    private TipoVendaEnums tipoVendaEnums;
    @NotNull
    @PositiveOrZero
    private BigDecimal precoCompra;
    @NotNull
    @Positive
    private BigDecimal precoVenda;
    @NotNull
    @PositiveOrZero
    private BigDecimal quantidadeEstoque;
    @NotNull
    @PositiveOrZero
    private BigDecimal estoqueMinimo;
}
