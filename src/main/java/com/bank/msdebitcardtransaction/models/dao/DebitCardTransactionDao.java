package com.bank.msdebitcardtransaction.models.dao;

import com.bank.msdebitcardtransaction.models.documents.DebitCardTransaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DebitCardTransactionDao extends ReactiveMongoRepository<DebitCardTransaction, String> {
}
