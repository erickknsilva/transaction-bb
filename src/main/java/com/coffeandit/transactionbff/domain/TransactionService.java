package com.coffeandit.transactionbff.domain;

import com.coffeandit.transactionbff.dto.RequestTransactionDto;
import com.coffeandit.transactionbff.dto.SituacaoEnum;
import com.coffeandit.transactionbff.dto.TransactionDto;
import com.coffeandit.transactionbff.feign.TransactionClient;
import com.coffeandit.transactionbff.redis.TransactionRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class TransactionService {

    @Value("${app.topic}")
    private String topic;


    private TransactionRedisRepository redisRepository;
    private final TransactionClient transactionClient;
    private RetryTemplate retryTemplate;
    private ReactiveKafkaProducerTemplate<String, RequestTransactionDto> reactiveKafkaProducerTemplate;

    public TransactionService(TransactionRedisRepository redisRepository, RetryTemplate retryTemplate,
                              ReactiveKafkaProducerTemplate<String, RequestTransactionDto> reactiveKafkaProducerTemplate,
                              TransactionClient transactionClient) {
        this.redisRepository = redisRepository;
        this.retryTemplate = retryTemplate;
        this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
        this.transactionClient = transactionClient;
    }

    @Retryable(retryFor = QueryTimeoutException.class, maxAttempts = 5, backoff = @Backoff(delay = 100, multiplier = 1.1))
    @Transactional
    public Mono<RequestTransactionDto> save(final RequestTransactionDto requestTransactionDto) {

        return Mono.fromCallable(() -> {

                    requestTransactionDto.setData(LocalDate.now());
                    requestTransactionDto.situacaoNaoAnalisa();
                    return redisRepository.save(requestTransactionDto);

                }).doOnError(throwable -> {
                    log.info(throwable.getMessage(), throwable);
                    throw new ResourceNotFoundException("Recurso não encontrado.");
                })
                .doOnSuccess(transaction -> {
                    log.info("Transação enviada com sucesso {}", transaction);
                    //ativa o kafka
                    reactiveKafkaProducerTemplate.send(topic, requestTransactionDto)
                            .doOnSuccess(voidSenderResult -> log.info(voidSenderResult.toString()))
                            .subscribe();
                })
                .doFinally(signalType -> {
                    if (signalType.compareTo(SignalType.ON_COMPLETE) == 0) {
                        log.info("Mensagem enviada para o kafka com sucesso 2 {}", requestTransactionDto);
                    }
                });
    }

    public Flux<List<TransactionDto>> findAllAgenciaAndConta(Long agencia, Long conta) {

        List<TransactionDto> allAgenciaAndConta = findByAgenciaAndConta(agencia, conta);

        return Flux.fromIterable(allAgenciaAndConta).cache(Duration.ofSeconds(5))
                .limitRate(200)
                .defaultIfEmpty(new TransactionDto())
                .buffer(200);

    }

    public List<TransactionDto> findByAgenciaAndConta(Long agencia, Long conta) {
        return transactionClient.findAllTransactions(agencia, conta);
    }

    @Retryable(retryFor = QueryTimeoutException.class, maxAttempts = 5, backoff = @Backoff(delay = 100, multiplier = 1.1))
    public Optional<TransactionDto> findById(final String id) {

        return retryTemplate.execute(retry -> {
            log.info("Consultando o Redis");
            return redisRepository.findById(id);
        });
    }

}