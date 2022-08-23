package com.bank.msdebitcardtransaction.services.impl;

import com.bank.msdebitcardtransaction.handler.ResponseHandler;
import com.bank.msdebitcardtransaction.models.utils.ResponseDebitCard;
import com.bank.msdebitcardtransaction.services.DebitCardService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class DebitCardServiceImpl implements DebitCardService {

    private final WebClient webClient;

    public DebitCardServiceImpl(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl("http://localhost:8089").build();
    }

    @Override
    public Mono<ResponseDebitCard> getDebitCard(String id) {
        return webClient.get()
                .uri("/api/debitcard/"+ id)
                .retrieve()
                .bodyToMono(ResponseDebitCard.class);
    }
}
