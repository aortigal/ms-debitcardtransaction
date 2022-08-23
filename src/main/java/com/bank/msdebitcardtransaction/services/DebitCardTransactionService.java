package com.bank.msdebitcardtransaction.services;

import com.bank.msdebitcardtransaction.handler.ResponseHandler;
import com.bank.msdebitcardtransaction.models.documents.DebitCardTransaction;
import reactor.core.publisher.Mono;

public interface DebitCardTransactionService {
    Mono<ResponseHandler> create(DebitCardTransaction d);

    Mono<ResponseHandler> findAll();

    Mono<ResponseHandler> find(String id);

    Mono<ResponseHandler> update(String id, DebitCardTransaction p);

    Mono<ResponseHandler> delete(String id);

    Mono<ResponseHandler> registerTransaction(DebitCardTransaction d);

}
