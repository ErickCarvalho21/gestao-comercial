package com.gestaocomercial.api.request;

import com.gestaocomercial.produto.dto.in.ProdutoAtualizacaoDTORequest;
import com.gestaocomercial.produto.enums.TipoVendaEnums;

import java.math.BigDecimal;

public class ProdutoAtualizacaoDTORequestFixture {

    public static ProdutoAtualizacaoDTORequest build(
            String nome,
            String codigoBarras,
            String descricao,
            TipoVendaEnums tipoVendaEnums,
            BigDecimal precoCompra,
            BigDecimal precoVenda,
            BigDecimal estoqueMinimo) {

        return new ProdutoAtualizacaoDTORequest(
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
