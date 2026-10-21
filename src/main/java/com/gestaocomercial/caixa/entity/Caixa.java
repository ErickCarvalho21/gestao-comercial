package com.gestaocomercial.caixa.entity;


import com.gestaocomercial.caixa.enums.StatusCaixaEnums;
import com.gestaocomercial.venda.entity.Venda;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Caixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCaixaEnums statusCaixaEnums;

    @Column(nullable = false)
    private BigDecimal saldoInicial;

    private BigDecimal saldoFinal;


    @OneToMany(mappedBy = "caixa")
    @Builder.Default
    private List<Venda> vendas = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime dataAbertura;

    private LocalDateTime dataFechamento;

    @PrePersist
    public void prePersist(){
        dataAbertura = LocalDateTime.now();
        statusCaixaEnums = StatusCaixaEnums.ABERTO;
    }



}
