package com.bank.msdebitcardtransaction.services;

import com.bank.msdebitcardtransaction.handler.ResponseHandler;
import com.bank.msdebitcardtransaction.models.utils.Movement;
import com.bank.msdebitcardtransaction.models.utils.ResponseDebitCard;
import com.bank.msdebitcardtransaction.models.utils.ResponseMovement;
import reactor.core.publisher.Mono;

public interface MovementService {

    Mono<ResponseMovement> saveMovement(Movement d);
}
