package com.example.shop.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class UserVo {
    private Long id;
    private String name;
    private String userid;
    private String email;
    private String password;
    private String address;
    private String phone;
    private String detail_address;
    private int addressnum;
}
