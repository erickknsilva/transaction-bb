package com.coffeandit.transactionbff.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(of = {"situacao"})
public class RequestTransactionDto extends TransactionDto {

    @JsonIgnore
    private SituacaoEnum situacao;

    @JsonIgnore
    private LocalDate data;
}


