package com.bank.msdebitcardtransaction.services.impl;

import com.bank.msdebitcardtransaction.handler.ResponseHandler;
import com.bank.msdebitcardtransaction.models.dao.DebitCardTransactionDao;
import com.bank.msdebitcardtransaction.models.documents.DebitCardTransaction;
import com.bank.msdebitcardtransaction.models.utils.Movement;
import com.bank.msdebitcardtransaction.models.utils.PasiveAmount;
import com.bank.msdebitcardtransaction.models.utils.ResponseMovement;
import com.bank.msdebitcardtransaction.services.DebitCardTransactionService;
import com.bank.msdebitcardtransaction.services.DebitCardService;
import com.bank.msdebitcardtransaction.services.MovementService;
import com.bank.msdebitcardtransaction.services.PasiveAmountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DebitCardTransactionServiceImpl implements DebitCardTransactionService {

    @Autowired
    private DebitCardTransactionDao dao;
    @Autowired
    private DebitCardService debitCardService;
    @Autowired
    private MovementService movementService;
    @Autowired
    private PasiveAmountService pasiveAmountService;

    private static final Logger log = LoggerFactory.getLogger(DebitCardTransactionServiceImpl.class);

    @Override
    public Mono<ResponseHandler> create(DebitCardTransaction d) {
        log.info("[INI] Create Debit Card Transaction");
        d.setDateOperation(LocalDateTime.now());
        return dao.save(d)
                .doOnNext(debitCard -> log.info(debitCard.toString()))
                .map(debitCard -> new ResponseHandler("Done", HttpStatus.OK, debitCard))
                .onErrorResume(error -> Mono.just(new ResponseHandler(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .doFinally(fin -> log.info("[END] Create Debit Card Transaction"));
    }

    @Override
    public Mono<ResponseHandler> findAll() {
        log.info("[INI] FindAll Debit Card Transaction");

        return dao.findAll()
                .doOnNext(debitCard -> log.info(debitCard.toString()))
                .collectList().map(debitCards -> new ResponseHandler("Done", HttpStatus.OK, debitCards))
                .onErrorResume(error -> Mono.just(new ResponseHandler(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .switchIfEmpty(Mono.just(new ResponseHandler("Empty", HttpStatus.NO_CONTENT, null)))
                .doFinally(fin -> log.info("[END] FindAll Debit Card Transaction"));

    }

    @Override
    public Mono<ResponseHandler> find(String id) {
        log.info("[INI] Find Debit Card");
        return dao.findById(id)
                .doOnNext(debitCard -> log.info(debitCard.toString()))
                .map(debitCard -> new ResponseHandler("Done", HttpStatus.OK, debitCard))
                .onErrorResume(error -> Mono.just(new ResponseHandler(error.getMessage(), HttpStatus.BAD_REQUEST, null)))
                .switchIfEmpty(Mono.just(new ResponseHandler("Empty", HttpStatus.NO_CONTENT, null)))
                .doFinally(fin -> log.info("[END] Find Debit Card"));
    }

    @Override
    public Mono<ResponseHandler> update(String id, DebitCardTransaction d) {
        log.info("[INI] update Debit Card Transaction");
        return dao.existsById(id).flatMap(check -> {
                    if (check){
                        d.setDateUpdateOperation(LocalDateTime.now());
                        return dao.save(d)
                                .doOnNext(debitCardTransaction -> log.info(debitCardTransaction.toString()))
                                .map(debitCardTransaction -> new ResponseHandler("Done", HttpStatus.OK, debitCardTransaction))
                                .onErrorResume(error -> Mono.just(new ResponseHandler(error.getMessage(), HttpStatus.BAD_REQUEST, null)));
                    }
                    else
                        return Mono.just(new ResponseHandler("Not found", HttpStatus.NOT_FOUND, null));

                })
                .doFinally(fin -> log.info("[END] Update Debit Card Transaction"));
    }

    @Override
    public Mono<ResponseHandler> delete(String id) {
        log.info("[INI] delete Debit Card Transaction");
        return dao.existsById(id).flatMap(check -> {
                    if (check)
                        return dao.deleteById(id).then(Mono.just(new ResponseHandler("Done", HttpStatus.OK, null)));
                    else
                        return Mono.just(new ResponseHandler("Not found", HttpStatus.NOT_FOUND, null));
                })
                .doFinally(fin -> log.info("[END] Delete Debit Card Transaction"));
    }

    @Override
    public Mono<ResponseHandler> registerTransaction(DebitCardTransaction d) {
        log.info("[INI] register Debit Card Transaction");
        log.info("Tarjeta ingresada " +d.getIdCard());
        return debitCardService.getDebitCard(d.getIdCard()).flatMap(x -> {
            log.info(x.getData().toString());
            if (x.getData().getAccounts().stream().count() == 0){
                return Mono.just(new ResponseHandler("Not found", HttpStatus.NOT_FOUND, null));
            }
            return pasiveAmountService.validPasiveAmount(x.getData().getAccounts(), d.getAmount())
                    .flatMap(y -> {
                        log.info("Account " + y.toString());
                        Movement movement = new Movement();
                        movement.setClientId(x.getData().getClientId());
                        movement.setAmount(d.getAmount());
                        movement.setTypeMovement("0");
                        movement.setPasiveId(y.getIdPasive());
                        log.info(movement.toString());
                        return movementService.saveMovement(movement).flatMap(z -> {
                            d.setDateOperation(LocalDateTime.now());
                            d.setDateUpdateOperation(LocalDateTime.now());
                            d.setAccount(z.getData().getPasiveId());
                            return dao.save(d).map(response -> new ResponseHandler("Done", HttpStatus.OK, response));
                        });
                    });

        });
    }

}
