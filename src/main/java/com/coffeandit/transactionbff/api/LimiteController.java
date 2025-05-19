package com.coffeandit.transactionbff.api;

import com.coffeandit.transactionbff.domain.LimiteService;
import com.coffeandit.transactionbff.dto.LimiteDiario;
import com.coffeandit.transactionbff.feign.LimiteClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

@RestController
@RequestMapping("/limite-diario")
public class LimiteController {


    private final LimiteService limiteService;

    public LimiteController(LimiteService limiteService) {
        this.limiteService = limiteService;
    }

    @GetMapping("/{agencia}/{conta}")
    public Mono<LimiteDiario> findByLimiteDiario(@PathVariable("agencia") final Long agencia,
                                                           @PathVariable("conta") final Long conta) {

        return limiteService.findByLimiteDiario(agencia, conta);
    }


}
