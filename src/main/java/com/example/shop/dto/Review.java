package com.example.shop.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Review {

    private int pd_id;
    private String merchant_uid;
    private String id;
    private String review;
    private int rate;
    private String reviewDate;

}
