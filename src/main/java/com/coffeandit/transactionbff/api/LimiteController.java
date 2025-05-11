package com.coffeandit.transactionbff.api;

import com.coffeandit.transactionbff.dto.LimiteDiario;
import com.coffeandit.transactionbff.feign.LimiteClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/limite-diario")
public class LimiteController {


    private final LimiteClient limiteClient;

    public LimiteController(LimiteClient limiteClient) {
        this.limiteClient = limiteClient;
    }

    @GetMapping("/{agencia}/{conta}")
    public LimiteDiario findByLimiteDiario(@PathVariable("agencia") final Long agencia,
                                           @PathVariable("conta") final Long conta) {

        return limiteClient.findByLimiteDiario(agencia, conta);
    }

}
