package com.bank.msdebitcardtransaction.services;

import com.bank.msdebitcardtransaction.models.utils.PasiveMont;
import com.bank.msdebitcardtransaction.models.utils.ResponsePasiveMont;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PasiveMontService {
    Mono<ResponsePasiveMont> findPasiveMont(String id);
    Flux<PasiveMont> listPasiveMont(List<String> accounts);
    Mono<PasiveMont> validPasiveMont(List<String> accounts, long amount);
}
