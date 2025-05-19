package com.coffeandit.transactionbff.feign;

import com.coffeandit.transactionbff.dto.LimiteDiario;
import com.coffeandit.transactionbff.dto.TransactionDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@org.springframework.cloud.openfeign.FeignClient(value = "transaction", url = "${transaction.url}")
public interface TransactionClient {

    @GetMapping(value = "/{agencia}/{conta}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<TransactionDto> findAllTransactions(@PathVariable("agencia") final Long agencia, @PathVariable("conta") final Long conta);

}

