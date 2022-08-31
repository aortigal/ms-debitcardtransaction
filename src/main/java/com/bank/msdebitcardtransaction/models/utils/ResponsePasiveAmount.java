package com.bank.msdebitcardtransaction.models.utils;

import lombok.Data;

@Data
public class ResponsePasiveAmount {
    private PasiveAmount data;
    private String message;
    private String status;
}
