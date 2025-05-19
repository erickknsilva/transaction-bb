package com.coffeandit.transactionbff.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "uuid")
//Objeto a ser persistido no redis e o tempo de vida
@RedisHash(value = "TransactionDto", timeToLive = 100)
public class TransactionDto {


    @Id
    @Schema(description = "Código de indentificação da transação.")
    private UUID uuid;

    @Schema(description = "Valor da transação.")
    @NotNull(message = "Informar o valor da transação.")
    private BigDecimal valor;

    @Schema(description = "Data/hora/minuto e segundo da transação")
    @JsonFormat(pattern =  "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate data;

    @NotNull(message = "Informa a conta de origem da transação.")
    @Schema(description = "Conta de origem da transação.")
    @Valid
    private Conta conta;

    @Schema(description = "Beneficiário da transação")
    @Valid
    private BeneficiarioDto beneficiario;

    @NotNull(message = "Informar o tipo da transação")
    @Schema(description = "Tipo de transação")
    private TipoTransacao tipoTransacao;

    @Schema(description = "Situação da transação")
    private SituacaoEnum situacao;

    public void situacaoNaoAnalisa(){
        setSituacao(SituacaoEnum.NAO_ANALISADA);
    }

}
