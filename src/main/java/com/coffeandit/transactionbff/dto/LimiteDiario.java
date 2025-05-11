package com.coffeandit.transactionbff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LimiteDiario {

    private Long id;

    private Long agencia;

    private Long conta;

    private BigDecimal valor;
}
