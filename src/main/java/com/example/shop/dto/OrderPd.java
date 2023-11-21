package com.example.shop.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class OrderPd {

    private String merchant_uid;
    private int pd_id;
    private int pd_count;
    private String pd_name;
    private int price;
    private String kr_price;

}
