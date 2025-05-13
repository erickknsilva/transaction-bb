package com.coffeandit.transactionbff.domain;


import com.coffeandit.transactionbff.dto.LimiteDiario;
import com.coffeandit.transactionbff.feign.LimiteClient;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@AllArgsConstructor
@Service
public class LimiteService {

    private LimiteClient limiteClient;

    @Autowired
    private CircuitBreaker timeCircuitBreaker;


    public LimiteDiario findByLimiteDiario(final Long agencia, final Long conta) {
        var result = fallback(agencia, conta);
        return result.get();
    }

    private Supplier<LimiteDiario> fallback(final Long agencia, final Long conta) {

        var result = timeCircuitBreaker
                .decorateSupplier(() -> limiteClient.findByLimiteDiario(agencia, conta));

        return Decorators
                .ofSupplier(result)
                .withCircuitBreaker(timeCircuitBreaker)
                .withFallback(List.of(CallNotPermittedException.class),
                        e -> this.getStaticLimite())
                .decorate();
    }


    private LimiteDiario getStaticLimite() {
        var limiteDiario = new LimiteDiario();
        limiteDiario.setValor(BigDecimal.ZERO);
        return limiteDiario;
    }
}
