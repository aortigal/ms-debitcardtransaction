package com.bank.msdebitcardtransaction.models.utils;

import lombok.Data;

@Data
public class ResponsePasiveMont {
    private PasiveMont data;
    private String message;
    private String status;
}
