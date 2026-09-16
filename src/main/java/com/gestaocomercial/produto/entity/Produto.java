package com.gestaocomercial.produto.entity;

import com.gestaocomercial.produto.enums.TipoVendaEnums;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

}
