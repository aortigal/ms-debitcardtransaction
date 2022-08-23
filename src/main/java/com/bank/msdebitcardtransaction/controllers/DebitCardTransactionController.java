package com.bank.msdebitcardtransaction.controllers;

import com.bank.msdebitcardtransaction.handler.ResponseHandler;
import com.bank.msdebitcardtransaction.models.documents.DebitCardTransaction;
import com.bank.msdebitcardtransaction.services.DebitCardTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/debitcardtransaction")
public class DebitCardTransactionController {

    @Autowired
    private DebitCardTransactionService debitCardTransactionService;

    @PostMapping
    public Mono<ResponseHandler> create(@Valid @RequestBody DebitCardTransaction d) {
        return debitCardTransactionService.create(d);
    }

    @PostMapping("transaction")
    public Mono<ResponseHandler> createTransaction(@Valid @RequestBody DebitCardTransaction d) {
        return debitCardTransactionService.registerTransaction(d);
    }

    @GetMapping
    public Mono<ResponseHandler> findAll() {
        return debitCardTransactionService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseHandler> find(@PathVariable String id) {
        return debitCardTransactionService.find(id);
    }


    @PutMapping("/{id}")
    public Mono<ResponseHandler> update(@PathVariable("id") String id,@Valid @RequestBody DebitCardTransaction d) {
        return debitCardTransactionService.update(id, d);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseHandler> delete(@PathVariable("id") String id) {
        return debitCardTransactionService.delete(id);
    }

}
