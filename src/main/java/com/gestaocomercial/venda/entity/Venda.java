package com.gestaocomercial.venda.entity;


import com.gestaocomercial.venda.enums.StatusVendaEnums;
import com.gestaocomercial.venda.enums.FormaPagamentoEnums;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "venda", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    private List<ItemVenda> itens = new ArrayList<>();
    @Column
    @Enumerated(EnumType.STRING)
    private StatusVendaEnums statusVendasEnums;
    @Column(nullable = false)
    private BigDecimal valorTotal;
    @Column
    @Enumerated(EnumType.STRING)
    private FormaPagamentoEnums formaPagamentosEnums;
    @Column(nullable = false)
    private LocalDateTime dataVenda;
    @PrePersist
    public void prePersist(){
        dataVenda = LocalDateTime.now();
        statusVendasEnums = StatusVendaEnums.ABERTA;
        valorTotal = BigDecimal.ZERO;

    }
}
