package com.bank.msdebitcardtransaction.models.utils;

import lombok.Data;

@Data
public class ResponseMovement {

    private Movement data;
    private String message;
    private String status;

}
