package com.example.shop.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class PaymentInfo {
    private String merchant_uid;
    private String name;
    private int amount;
    private String buyer_email;
    private String buyer_name;
    private String buyer_tel;
    private String buyer_addr;
    private String buyer_addDetail;
    private String buyer_postcode;
    private String kr_price;
}
