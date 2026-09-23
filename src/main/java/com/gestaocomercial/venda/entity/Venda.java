package com.gestaocomercial.venda.entity;


import com.gestaocomercial.venda.enums.StatusVendaEnums;
import com.gestaocomercial.venda.enums.TipoPagamentoEnums;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    @OneToMany(mappedBy = "venda")
    private List<ItemVenda> itens;
    @Column
    @Enumerated(EnumType.STRING)
    private StatusVendaEnums statusVendasEnums;
    @Column(nullable = false)
    private BigDecimal valorVenda;
    @Column
    @Enumerated(EnumType.STRING)
    private TipoPagamentoEnums tipoPagamentosEnums;
    @Column(nullable = false)
    private LocalDateTime dataVenda;
    @PrePersist
    public void prePersist(){
        dataVenda = LocalDateTime.now();
    }
}
