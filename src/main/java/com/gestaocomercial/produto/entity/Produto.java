package com.gestaocomercial.produto.entity;

import com.gestaocomercial.produto.enums.TipoVendaEnums;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Produto {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false,unique = true)
    private String codigoBarras;
    private String descricao;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoVendaEnums tipoVendaEnums;
    @Column(nullable = false)
    private BigDecimal precoCompra;
    @Column(nullable = false)
    private BigDecimal precoVenda;
    @Column(nullable = false)
    private BigDecimal quantidadeEstoque;
    @Column(nullable = false)
    private BigDecimal estoqueMinimo;
    @Column(nullable = false)
    private boolean ativo;
    @Column(nullable = false)
    private LocalDateTime dataCadastro;
    @PrePersist
    public void prePersist(){
        dataCadastro = LocalDateTime.now();
        ativo = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Produto produto = (Produto) o;

        return ativo == produto.ativo
                && Objects.equals(id, produto.id)
                && Objects.equals(nome, produto.nome)
                && Objects.equals(codigoBarras, produto.codigoBarras)
                && Objects.equals(descricao, produto.descricao)
                && tipoVendaEnums == produto.tipoVendaEnums
                && Objects.equals(precoCompra, produto.precoCompra)
                && Objects.equals(precoVenda, produto.precoVenda)
                && Objects.equals(quantidadeEstoque, produto.quantidadeEstoque)
                && Objects.equals(estoqueMinimo, produto.estoqueMinimo)
                && Objects.equals(dataCadastro, produto.dataCadastro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, codigoBarras, descricao, tipoVendaEnums,
                precoCompra, precoVenda, quantidadeEstoque, estoqueMinimo, ativo, dataCadastro
        );
    }


}
