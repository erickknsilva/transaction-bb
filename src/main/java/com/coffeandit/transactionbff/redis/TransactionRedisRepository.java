package com.coffeandit.transactionbff.redis;

import com.coffeandit.transactionbff.dto.TransactionDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRedisRepository
        extends CrudRepository<TransactionDto, String> {



}
