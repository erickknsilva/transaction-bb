package com.coffeandit.transactionbff.config;


import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class CircuitBreakerConfiguration {


    @Bean
    public CircuitBreaker countCircuitBreaker(){

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED) // tipo de janela deslizante baseada em contagem de chamadas
                .slidingWindowSize(20) // número de chamadas usadas para calcular a taxa de falhas
                .slowCallRateThreshold(65.0F) // porcentagem de chamadas lentas (acima do threshold de duração) para acionar o circuito
                .slowCallDurationThreshold(Duration.ofSeconds(2)) // tempo a partir do qual uma chamada é considerada "lenta"
                .waitDurationInOpenState(Duration.ofMillis(3500)) // tempo que o circuito permanece aberto antes de ir para o estado HALF_OPEN
                .permittedNumberOfCallsInHalfOpenState(5) // número de chamadas permitidas no estado HALF_OPEN para testar se o serviço se recuperou
                .build();

        //registra o circuitBreaker
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);

        //nomeia o circuitBreaker
        return circuitBreakerRegistry.circuitBreaker("limiteSearchBasedOnCount");


    }

    @Bean
    public CircuitBreaker timeCircuitBreaker() {

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED) // janela baseada em tempo
                .slidingWindowSize(10) // duração da janela em segundos
                .minimumNumberOfCalls(5) // chamadas mínimas para avaliar falha
                .failureRateThreshold(55.0F) // % de falhas para abrir o circuito
                .waitDurationInOpenState(Duration.ofMillis(1500)) // tempo em estado OPEN
                .writableStackTraceEnabled(false) // desativa stacktrace por performance
                .build();

        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
        return circuitBreakerRegistry.circuitBreaker("limiteSearchBasedOnTime");
    }


}
