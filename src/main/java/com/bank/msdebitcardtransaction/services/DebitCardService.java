package com.bank.msdebitcardtransaction.services;

import com.bank.msdebitcardtransaction.models.utils.ResponseDebitCard;
import reactor.core.publisher.Mono;

public interface DebitCardService {

    Mono<ResponseDebitCard> getDebitCard(String d);

}
