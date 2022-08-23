package com.bank.msdebitcardtransaction.services.impl;

import com.bank.msdebitcardtransaction.handler.ResponseHandler;
import com.bank.msdebitcardtransaction.models.utils.Movement;
import com.bank.msdebitcardtransaction.models.utils.ResponseDebitCard;
import com.bank.msdebitcardtransaction.models.utils.ResponseMovement;
import com.bank.msdebitcardtransaction.services.MovementService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class MovementServiceImpl implements MovementService {

    private final WebClient webClient;

    public MovementServiceImpl(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl("http://localhost:8084").build();
    }

    @Override
    public Mono<ResponseMovement> saveMovement(Movement d) {
        return webClient.post()
                .uri("/api/movement/")
                .body(Mono.just(d), ResponseMovement.class)
                .retrieve()
                .bodyToMono(ResponseMovement.class);
    }
}
