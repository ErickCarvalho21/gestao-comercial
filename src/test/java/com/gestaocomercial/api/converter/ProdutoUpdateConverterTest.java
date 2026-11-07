package com.gestaocomercial.api.converter;

import com.gestaocomercial.api.request.ProdutoAtualizacaoDTORequestFixture;
import com.gestaocomercial.produto.Converter.ProdutoConverter;
import com.gestaocomercial.produto.dto.in.ProdutoAtualizacaoDTORequest;
import com.gestaocomercial.produto.entity.Produto;
import com.gestaocomercial.produto.enums.TipoVendaEnums;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ProdutoUpdateConverterTest {

    @InjectMocks
    private ProdutoConverter produtoConverter;

    private Produto produtoEntity;
    private ProdutoAtualizacaoDTORequest produtoAtualizacaoDTORequest;
    private Produto produtoEntityEsperado;

    private LocalDateTime dataHora;

    @BeforeEach
    void setup() {
        dataHora = LocalDateTime.of(2023, 10, 5, 14, 15);

        produtoEntity = Produto.builder()
                .id(1L)
                .nome("Arroz")
                .codigoBarras("7891234567890")
                .descricao("Pacote de arroz de 1 kg")
                .tipoVendaEnums(TipoVendaEnums.UNIDADE)
                .precoCompra(new BigDecimal("4.00"))
                .precoVenda(new BigDecimal("6.00"))
                .quantidadeEstoque(new BigDecimal("10"))
                .estoqueMinimo(new BigDecimal("5"))
                .ativo(true)
                .dataCadastro(dataHora)
                .build();

        produtoAtualizacaoDTORequest =
                ProdutoAtualizacaoDTORequestFixture.build(
                        "Arroz a granel",
                        "7899876543210",
                        "Arroz vendido por peso",
                        TipoVendaEnums.PESO,
                        new BigDecimal("3.00"),
                        new BigDecimal("5.00"),
                        new BigDecimal("8")
                );

        produtoEntityEsperado = Produto.builder()
                .id(1L)
                .nome("Arroz a granel")
                .codigoBarras("7899876543210")
                .descricao("Arroz vendido por peso")
                .tipoVendaEnums(TipoVendaEnums.PESO)
                .precoCompra(new BigDecimal("3.00"))
                .precoVenda(new BigDecimal("5.00"))
                .quantidadeEstoque(new BigDecimal("10"))
                .estoqueMinimo(new BigDecimal("8"))
                .ativo(true)
                .dataCadastro(dataHora)
                .build();
    }

    @Test
    void deveAtualizarProdutoEntity() {
        Produto entity = produtoConverter.updateProduto(
                produtoAtualizacaoDTORequest,
                produtoEntity
        );

        assertEquals(produtoEntityEsperado, entity);
    }
}

