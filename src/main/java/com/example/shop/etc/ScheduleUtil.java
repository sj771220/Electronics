package com.example.shop.etc;


import com.example.shop.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("ScheduleUtil")
public class ScheduleUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(ScheduleUtil.class);

    @Autowired
    private ShopService shopService;

    @Scheduled(cron = "0 0 2 * * ?")
    public void ClearCart() {

        LOGGER.debug("##### Schedule start...######");
        LOGGER.debug("##### Clear UnOrdered CartList...######");


    }

    @Scheduled(cron = "0 0 9 * * MON")
    public void SendNewsLetter() {

        LOGGER.debug("##### Schedule start...######");

    }





}
