package com.bank.msdebitcardtransaction.services.impl;

import com.bank.msdebitcardtransaction.models.utils.PasiveMont;
import com.bank.msdebitcardtransaction.models.utils.ResponsePasiveMont;
import com.bank.msdebitcardtransaction.services.PasiveMontService;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class PasiveMontServiceImpl implements PasiveMontService {

    private final WebClient webClient;

    private static final Logger log = LoggerFactory.getLogger(PasiveMontServiceImpl.class);

    public PasiveMontServiceImpl(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl("http://localhost:8082").build();
    }

    @Override
    public Mono<ResponsePasiveMont> findPasiveMont(String id) {
        return webClient.get()
                .uri("/api/pasive/mont/" + id)
                .retrieve()
                .bodyToMono(ResponsePasiveMont.class);
    }

    @Override
    public Flux<PasiveMont> listPasiveMont(List<String> accounts) {
        log.info("INI listado");
        List<PasiveMont> pasiveMontListFlux = new ArrayList<PasiveMont>();
        for (String account : accounts) {
            PasiveMont pasive = new PasiveMont();
            pasive.setIdPasive(account);
            pasiveMontListFlux.add(pasive);
        }
        Flux<PasiveMont> pasiveMontFlux = Flux.fromIterable(pasiveMontListFlux) ;

        return pasiveMontFlux.flatMap(p ->{
            return findPasiveMont(p.getIdPasive()).flatMap(f ->{
                p.setMont(f.getData().getMont());
                return Mono.just(p);
            });
        });
    }


    @Override
    public Mono<PasiveMont> validPasiveMont(List<String> accounts, long amount) {
        log.info("Accounts : " + accounts.toString());
        return listPasiveMont(accounts)
                .filter(f -> f.getMont()>amount).next();

    }



/*
    @Override
    public Mono<PasiveMont> validPasiveMont(List<String> accounts, long amount){
        log.info("Accounts : " + accounts.toString());
        PasiveMont pasiveMontsValid = new PasiveMont();
        AtomicBoolean found = new AtomicBoolean(false);
        return Mono.just(pasiveMontsValid).flatMap(z ->{
           accounts.forEach(x ->{
                  findPasiveMont(x).flatMap(y -> {
                    log.info("Monto " +y.getData().getMont());
                    if (y.getData().getMont() > amount && !found.get()){
                        log.info("validacion iompleta");

                        z.setIdPasive(y.getData().getIdPasive());
                        z.setMont(y.getData().getMont());
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
