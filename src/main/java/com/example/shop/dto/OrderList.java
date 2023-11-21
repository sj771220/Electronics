package com.example.shop.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class OrderList {

    private String merchant_uid;
    private Long userid;
    private int amount;
    private String delivery_status;
    private String buyer_email;
    private String buyer_address;
    private String buyer_addDetail;
    private int buyer_postcode;
    private String buyer_phone;
    private String buyer_name;
    private String orderdate;
    private String pd_nameex;
    private int merchant_count;
    private List<OrderPd> pdlist;
    private String kr_price;
}
