package com.coffeandit.transactionbff.domain;

import com.coffeandit.transactionbff.dto.RequestTransactionDto;
import com.coffeandit.transactionbff.dto.TransactionDto;
import com.coffeandit.transactionbff.redis.TransactionRedisRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRedisRepository redisRepository;

    public TransactionService(TransactionRedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    @Transactional
    public Optional<TransactionDto> save(final RequestTransactionDto requestTransactionDto) {
        requestTransactionDto.setData(LocalDateTime.now());
        return Optional.of(redisRepository.save(requestTransactionDto));
    }

    @Transactional
    public Optional<TransactionDto> findById(final String id) {

        return redisRepository.findById(id);
    }


}
