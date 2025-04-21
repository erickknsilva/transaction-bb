package com.coffeandit.transactionbff;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestTransactionDto extends TransactionDto {

    @JsonIgnore
    private SituacaoEnum situacao;

    @JsonIgnore
    private LocalDateTime data;
}


