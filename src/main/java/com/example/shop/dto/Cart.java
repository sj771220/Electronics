package com.example.shop.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Cart {

    private String userid;
    private int pd_count;
    private int id;
    private String pd_name;
    private int pd_price;
    private int pd_sale_percent;
    private String pd_img;
    private String kr_price;
}
