package com.gestaocomercial.estoque.entity;

import com.gestaocomercial.estoque.enums.TipoMovimentacaoEnums;
import com.gestaocomercial.produto.entity.Produto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    @Enumerated(EnumType.STRING)
    @Column
    private TipoMovimentacaoEnums tipoMoviementacaoEnums;
    @Column(nullable = false)
    private BigDecimal quantidade;
    @Column(nullable = false)
    private LocalDateTime data;
    @PrePersist
    public void prePersist(){
        data = LocalDateTime.now();
    }
    
}
