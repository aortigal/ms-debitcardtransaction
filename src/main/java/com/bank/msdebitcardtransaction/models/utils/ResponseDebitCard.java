package com.bank.msdebitcardtransaction.models.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ResponseDebitCard {

    private DebitCard data;
    private String message;
    private String status;

}
