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
    public ResponseEntity<String> receivePaymentInfo(@RequestBody PaymentInfo paymentInfo) {

        Long loginid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String merchant=paymentInfo.getMerchant_uid();
        int amount=paymentInfo.getAmount();
        String delivery="배송 전";
        String buyer_email=paymentInfo.getBuyer_email();
        String buyer_address=paymentInfo.getBuyer_addr();
        String buyer_addDetail=paymentInfo.getBuyer_addDetail();
        int buyer_postcode=Integer.parseInt(paymentInfo.getBuyer_postcode());
        String buyer_phone=paymentInfo.getBuyer_tel();
        String buyer_name=paymentInfo.getBuyer_name();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate today=LocalDate.now();



        shopService.checkoutFinish1(merchant,loginid,amount,delivery,buyer_email,buyer_address,buyer_addDetail,buyer_postcode,buyer_phone,buyer_name,today);
        List<Cart>list= shopService.showCart2(loginid);
        for(int i=0;i<list.size();i++){
            shopService.checkoutFinish2(merchant,list.get(i).getId(),list.get(i).getPd_count());
        }

        shopService.checkoutFinish3(loginid);

        List<OrderPd> pdlist=shopService.getorderPd(paymentInfo.getMerchant_uid());

        for(int i=0;i<pdlist.size();i++){
            shopService.updateorder_count(pdlist.get(i).getPd_id(),pdlist.get(i).getPd_count());
            System.out.println("-=========================================");
            System.out.println(pdlist);

        }



        return ResponseEntity.ok("Success");
    }


}