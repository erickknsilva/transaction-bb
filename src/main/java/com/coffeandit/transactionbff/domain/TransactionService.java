package com.coffeandit.transactionbff.domain;

import com.coffeandit.transactionbff.dto.RequestTransactionDto;
import com.coffeandit.transactionbff.dto.TransactionDto;
import com.coffeandit.transactionbff.redis.TransactionRedisRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@Slf4j
@Service
public class TransactionService {

    @Value("${app.topic}")
    private String topic;


    private TransactionRedisRepository redisRepository;
    private RetryTemplate retryTemplate;
    private ReactiveKafkaProducerTemplate<String, RequestTransactionDto> reactiveKafkaProducerTemplate;

    public TransactionService(TransactionRedisRepository redisRepository, RetryTemplate retryTemplate,
                              ReactiveKafkaProducerTemplate<String, RequestTransactionDto> reactiveKafkaProducerTemplate) {
        this.redisRepository = redisRepository;
        this.retryTemplate = retryTemplate;
        this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
    }

    @Retryable(retryFor = QueryTimeoutException.class, maxAttempts = 5, backoff = @Backoff(delay = 100, multiplier = 1.1))
    @Transactional
    public Optional<TransactionDto> save(final RequestTransactionDto requestTransactionDto) {
        requestTransactionDto.setData(LocalDateTime.now());
        //ativa o kafka
        reactiveKafkaProducerTemplate.send(topic, requestTransactionDto)
                .doOnSuccess(voidSenderResult -> log.info(voidSenderResult.toString()))
                .subscribe();

        return Optional.of(redisRepository.save(requestTransactionDto));
    }


    @Retryable(retryFor = QueryTimeoutException.class, maxAttempts = 5, backoff = @Backoff(delay = 100, multiplier = 1.1))
    public Optional<TransactionDto> findById(final String id) {

        return retryTemplate.execute(retry -> {
            log.info("Consultando o Redis");
            return redisRepository.findById(id);
        });
    }

}