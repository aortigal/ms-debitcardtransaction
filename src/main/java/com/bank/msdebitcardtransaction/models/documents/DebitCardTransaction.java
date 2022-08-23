package com.bank.msdebitcardtransaction.models.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(value = "debitcardtransaction")
public class DebitCardTransaction {
    @Id
    private String id  ;
    private LocalDateTime dateOperation;
    private LocalDateTime dateUpdateOperation;
    private String idCard;
    private String account;
    private long amount;
}
