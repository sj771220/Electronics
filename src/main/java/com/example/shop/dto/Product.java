package com.example.shop.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Product {

    private int id;
    private String pd_name;
    private int pd_price;
    private String pd_img;
    private int pd_sale_percent;
    private int pd_newprice;
    private String category;
    private int order_count;
    private int inventory;
    private int view;
    private int rating;
    private String description;
    private String kr_price;
}
