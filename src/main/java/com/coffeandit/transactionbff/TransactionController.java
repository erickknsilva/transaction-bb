package com.coffeandit.transactionbff;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.awt.*;

@RestController
@RequestMapping
public class TransactionController {

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TransactionDto> enviarTransacao(@RequestBody final RequestTransaction request) {

        return Mono.empty();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TransactionDto> buscarTransacao(@PathVariable final String uuid) {
        return Mono.empty();
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TransactionDto> deleteTransaction(@PathVariable final String uuid) {
        return Mono.empty();
    }

    @PatchMapping("/{id}/confimar")
    public Mono<TransactionDto>confirmaTransaction(@PathVariable("id")final String uuid){
        return Mono.empty();
    }

}
