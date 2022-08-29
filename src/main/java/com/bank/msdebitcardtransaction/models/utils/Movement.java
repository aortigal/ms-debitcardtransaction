package com.bank.msdebitcardtransaction.models.utils;

import lombok.Data;

@Data
public class Movement {

    private String pasiveId;
    private String clientId;
    private String typeMovement;
    private float amount;
}
