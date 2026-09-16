package com.gestaocomercial.produto.dto.out;

import com.gestaocomercial.produto.enums.TipoVendaEnums;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProdutoDTOResponse {

    private Long id;
    private String nome;
    private String codigoBarras;
    private String descricao;
    private TipoVendaEnums tipoVendaEnums;
    private BigDecimal precoCompra;
    private BigDecimal precoVenda;
    private BigDecimal quantidadeEstoque;
    private BigDecimal estoqueMinimo;
    private boolean ativo;
    private LocalDateTime dataCadastro;


}
