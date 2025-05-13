package com.coffeandit.transactionbff.domain;

import com.coffeandit.transactionbff.config.RetryConfiguration;
import com.coffeandit.transactionbff.dto.RequestTransactionDto;
import com.coffeandit.transactionbff.dto.TransactionDto;
import com.coffeandit.transactionbff.redis.TransactionRedisRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@Slf4j
@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRedisRepository redisRepository;

    private final RetryTemplate retryTemplate;


    @Retryable(retryFor = QueryTimeoutException.class, maxAttempts = 5, backoff = @Backoff(delay = 100, multiplier = 1.1))
    @Transactional
    public Optional<TransactionDto> save(final RequestTransactionDto requestTransactionDto) {
        requestTransactionDto.setData(LocalDateTime.now());
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