package com.gestaocomercial.venda.entity;

import com.gestaocomercial.produto.entity.Produto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;
    @Column(nullable = false)
    private BigDecimal quantidadeItem;
    @Column(nullable = false)
    private BigDecimal precoItemVenda;
    @Column(nullable = false)
    private BigDecimal totalItemVenda;

}
