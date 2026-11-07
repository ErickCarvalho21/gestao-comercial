package com.gestaocomercial.api.request;

import com.gestaocomercial.produto.dto.in.ProdutoDTORequest;
import com.gestaocomercial.produto.enums.TipoVendaEnums;

import java.math.BigDecimal;

public class ProdutoRequestDTOFixture {

    public static ProdutoDTORequest build(
            String nome,
            String codigoBarras,
            String descricao,
            TipoVendaEnums tipoVendaEnums,
            BigDecimal precoCompra,
            BigDecimal precoVenda,
            BigDecimal estoqueMinimo) {

        return new ProdutoDTORequest(
                nome,
                codigoBarras,
                descricao,
                tipoVendaEnums,
                precoCompra,
                precoVenda,
                estoqueMinimo
        );
    }
}
