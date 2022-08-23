package com.bank.msdebitcardtransaction.models.utils;

import lombok.Data;

import java.util.List;

@Data
public class DebitCard {
    private String id;
    private String cardNumber;
    private String expirationDate;
    private String clientId;
    private List<String> accounts;
}
