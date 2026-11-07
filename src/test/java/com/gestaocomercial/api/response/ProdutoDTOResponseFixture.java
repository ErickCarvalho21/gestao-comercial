package com.gestaocomercial.api.response;

import com.gestaocomercial.produto.dto.out.ProdutoDTOResponse;
import com.gestaocomercial.produto.enums.TipoVendaEnums;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProdutoDTOResponseFixture {
    public static ProdutoDTOResponse build(
            Long id,
            String nome,
            String codigoBarras,
            String descricao,
            TipoVendaEnums tipoVendaEnums,
            BigDecimal precoCompra,
            BigDecimal precoVenda,
            BigDecimal quantidadeEstoque,
            BigDecimal estoqueMinimo,
            boolean isAtivo,
            LocalDateTime dataCadastro) {

        return new ProdutoDTOResponse(
                id,
                nome,
                codigoBarras,
                descricao,
                tipoVendaEnums,
                precoCompra,
                precoVenda,
                quantidadeEstoque,
                estoqueMinimo,
                isAtivo,
                dataCadastro
        );
    }
}
