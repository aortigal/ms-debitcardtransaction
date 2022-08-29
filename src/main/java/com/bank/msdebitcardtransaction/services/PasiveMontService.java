package com.bank.msdebitcardtransaction.services;

import com.bank.msdebitcardtransaction.models.utils.PasiveAmount;
import com.bank.msdebitcardtransaction.models.utils.ResponsePasiveAmount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PasiveAmountService {
    Mono<ResponsePasiveAmount> findPasiveAmount(String id);
    Flux<PasiveAmount> listPasiveAmount(List<String> accounts);
    Mono<PasiveAmount> validPasiveAmount(List<String> accounts, long amount);
}
