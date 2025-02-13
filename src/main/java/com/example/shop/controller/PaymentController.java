package com.example.shop.controller;

import com.example.shop.dto.Cart;
import com.example.shop.dto.OrderPd;
import com.example.shop.dto.PaymentInfo;
import com.example.shop.service.ShopService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
public class PaymentController {

    @Value("${iamport.key}")
    private String restApiKey;
    @Value("${iamport.secret}")
    private String restApiSecret;
    @Autowired
    ShopService shopService;
    private IamportClient iamportClient;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(restApiKey, restApiSecret);
    }


    @PostMapping("/PaymentCheck")
    public ResponseEntity<String> receivePaymentInfo(@RequestBody Map<String,Object> paymentInfo) {

        Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        String merchant=String.valueOf(paymentInfo.get("merchant_uid"));

        LocalDate today=LocalDate.now();

        paymentInfo.put("delivery","배송 전");
        paymentInfo.put("loginid",loginid);
        paymentInfo.put("today",today);



        shopService.insertOrderList(paymentInfo);

        List<Cart>list= shopService.getCartListByUserID(loginid);

        for(int i=0;i<list.size();i++){
            shopService.insertOrderPD(merchant,list.get(i).getId(),list.get(i).getPd_count());
        }

        shopService.deleteFromCart(loginid);

        List<OrderPd> pdlist=shopService.getOrderPD(merchant);

        for(int i=0;i<pdlist.size();i++){
            shopService.updateorder_count(pdlist.get(i).getPd_id(),pdlist.get(i).getPd_count());

        }



        return ResponseEntity.ok("Success");
    }


}