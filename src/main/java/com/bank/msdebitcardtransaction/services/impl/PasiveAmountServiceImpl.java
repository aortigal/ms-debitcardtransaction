package com.bank.msdebitcardtransaction.services.impl;

import com.bank.msdebitcardtransaction.models.utils.PasiveAmount;
import com.bank.msdebitcardtransaction.models.utils.ResponsePasiveAmount;
import com.bank.msdebitcardtransaction.services.PasiveAmountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class PasiveAmountServiceImpl implements PasiveAmountService {

    private final WebClient webClient;

    private static final Logger log = LoggerFactory.getLogger(PasiveAmountServiceImpl.class);

    public PasiveAmountServiceImpl(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl("http://localhost:8082").build();
    }

    @Override
    public Mono<ResponsePasiveAmount> findPasiveAmount(String id) {
        return webClient.get()
                .uri("/api/pasive/amount/" + id)
                .retrieve()
                .bodyToMono(ResponsePasiveAmount.class);
    }

    @Override
    public Flux<PasiveAmount> listPasiveAmount(List<String> accounts) {
        log.info("INI listado");
        List<PasiveAmount> pasiveAmountListFlux = new ArrayList<PasiveAmount>();
        for (String account : accounts) {
            PasiveAmount pasive = new PasiveAmount();
            pasive.setIdPasive(account);
            pasiveAmountListFlux.add(pasive);
        }
        Flux<PasiveAmount> pasiveAmountFlux = Flux.fromIterable(pasiveAmountListFlux) ;

        return pasiveAmountFlux.flatMap(p ->{
            return findPasiveAmount(p.getIdPasive()).flatMap(f ->{
                p.setAmount(f.getData().getAmount());
                return Mono.just(p);
            });
        });
    }


    @Override
    public Mono<PasiveAmount> validPasiveAmount(List<String> accounts, long amount) {
        log.info("Accounts : " + accounts.toString());
        return listPasiveAmount(accounts)
                .filter(f -> f.getAmount()>amount).next();

    }



/*
    @Override
    public Mono<PasiveAmount> validPasiveAmount(List<String> accounts, long amount){
        log.info("Accounts : " + accounts.toString());
        PasiveAmount pasiveAmountsValid = new PasiveAmount();
        AtomicBoolean found = new AtomicBoolean(false);
        return Mono.just(pasiveAmountsValid).flatMap(z ->{
           accounts.forEach(x ->{
                  findPasiveAmount(x).flatMap(y -> {
                    log.info("Amounto " +y.getData().getAmount());
                    if (y.getData().getAmount() > amount && !found.get()){
                        log.info("validacion iompleta");

                        z.setIdPasive(y.getData().getIdPasive());
                        z.setAmount(y.getData().getAmount());
                        log.info("validacion incompleta");

                        found.set(true);
                        log.info("validacion completa");
                    }
                    log.info("Despues de if");
                    return Mono.just(z);
                }).subscribe();
                log.info("despues del subscribe");
            });
            log.info("sin valores");
            return null;
        });

    }*/
}
