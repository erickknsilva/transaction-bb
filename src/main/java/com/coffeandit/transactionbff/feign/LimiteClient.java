package com.coffeandit.transactionbff.feign;

import com.coffeandit.transactionbff.dto.LimiteDiario;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@org.springframework.cloud.openfeign.FeignClient(value = "limites", url = "${limites.url}")
public interface LimiteClient {

    @GetMapping(value = "/{agencia}/{conta}", produces = MediaType.APPLICATION_JSON_VALUE)
    LimiteDiario findByLimiteDiario(@PathVariable("agencia") final Long agencia, @PathVariable("conta") final Long conta);
}

