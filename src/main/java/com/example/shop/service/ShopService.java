package com.example.shop.service;


import com.example.shop.dao.ShopDao;
import com.example.shop.dto.Cart;
import com.example.shop.dto.OrderPd;
import com.example.shop.dto.Product;
import com.example.shop.dto.UserVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ShopService {

    private final ShopDao shopdao;
    @Autowired
    public ShopService(ShopDao shopDao){this.shopdao=shopDao;}


    public List<Product> calculprice(List<Product>list){
        for(int i=0;i<list.size();i++){
            int oldprice=list.get(i).getPd_price();
            int percent=list.get(i).getPd_sale_percent();
            int newprice=oldprice-(oldprice*percent/100);
            list.get(i).setPd_newprice(newprice);

            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);

            // 숫자를 형식화하여 문자열로 변환
            String formattedAmount = numberFormat.format(newprice);


            list.get(i).setKr_price(formattedAmount);
        }
        return list;
    }

    public List<Cart> calculprice2(List<Cart>list){
        for(int i=0;i<list.size();i++){
            int oldprice=list.get(i).getPd_price();
            int percent=list.get(i).getPd_sale_percent();
            int newprice=oldprice-(oldprice*percent/100);
            int total=newprice*list.get(i).getPd_count();
            list.get(i).setPd_price(total);


            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);

            // 숫자를 형식화하여 문자열로 변환
            String formattedAmount = numberFormat.format(newprice);


            list.get(i).setKr_price(formattedAmount);

        }

        return list;
    }

    public List<Product> gettest(){ List<Product> list=shopdao.getBest();

        List<Product>listre=calculprice(list);
        return listre;}

    public List<Product> getlist(String which){
        List<Product> list=new ArrayList<>();
        switch (which){
            case "view": list=shopdao.viewList();
            break;
            case "top": list=shopdao.topList();
            break;
            case "rating": list=shopdao.ratingList();
            break;
        }

        List<Product> listre=calculprice(list);
        return listre;
    }

    public Product getProduct(int id){
        return shopdao.getProduct(id);
    }

    public List<Product> getCategoryList(String category){
        List<Product>list=shopdao.getCategoryList(category);

        return calculprice(list);

    }
    public int getCnt(){return shopdao.getCnt();}

    public List<Product> findListPaging(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize){
        List<Product> list=shopdao.findListPaging(startIndex,pageSize);

        return calculprice(list);
    }
    public List<Product>getCategoryPaging(@Param("category")String category,@Param("startIndex") int startIndex, @Param("pageSize") int pageSize ){
        List<Product> list=shopdao.getCategoryPaging(category,startIndex,pageSize);
        return calculprice(list);
    }

    public int getCategoryCnt(String category){
        return shopdao.getCategoryCnt(category);
    }

    public void insertCart(Long userid,int pd_count, int pd_id){
    shopdao.insertCart(userid,pd_count,pd_id);
    }

    public int aleadyInsert(Long userid,int pd_id){
         return shopdao.aleadyInsert(userid,pd_id);
    }

    public void extraCart(int count, Long userid, int pd_id){
        shopdao.extraCart(count,userid,pd_id);
    }

    public int getpdCnt(int pd_id){
        return shopdao.getpdCnt(pd_id);
    }

    public Product showCart1(int id){
        return shopdao.showCart1(id);
    }

    public List<Cart> showCart2(Long id){
        List<Cart>list=shopdao.showCart2(id);



        return calculprice2(list);
    }

    public List<Product>searchKeyword(@Param("param") String param,@Param("startIndex") int startIndex, @Param("pageSize") int pageSize){
        List<Product>list=calculprice(shopdao.searchKeyword(param,startIndex,pageSize));
        return list;
    }

    public int searchCnt(@Param("param") String param){
        return shopdao.searchCnt(param);
    }

    public void removeCart(Long userid, int id){
        shopdao.removeCart(userid,id);
    }

    public void pluscart(Long userid, int id){
        shopdao.pluscart(userid,id);
    }

    public void minuscart(Long userid,int id){
        shopdao.minuscart(userid,id);
    }

    public void inventoryCart1(int pd_count,int id){
        shopdao.inventoryCart1(pd_count,id);
    }

    public void inventoryCart2(int pd_count,int id){
        shopdao.inventoryCart2(pd_count,id);
    }

    public UserVo getUserInfo(Long id){
           return shopdao.getUserInfo(id);
    }

    public void checkoutFinish1(String merchant_uid, Long userid, int amount, String delivery_status, String buyer_email, String buyer_address, String buyer_addDetail, int buyer_postcode, String buyer_phone, String buyer_name, LocalDate today){
        shopdao.checkoutFinish1(merchant_uid,userid,amount,delivery_status,buyer_email,buyer_address,buyer_addDetail,buyer_postcode,buyer_phone,buyer_name,today);
    }

    public void checkoutFinish2(String merchant_uid,int pd_id,int pd_count){
        shopdao.checkoutFinish2(merchant_uid,pd_id,pd_count);
    }

    public void checkoutFinish3(Long userid){
        shopdao.checkoutFinish3(userid);
    }
    public void updateView(int id){
        shopdao.updateView(id);
    }

    public String getpd_name(int pd_id){
        return shopdao.getpd_name(pd_id);
    }

    public List<OrderPd> getorderPd(String merchant_uid){
        return shopdao.getorderPd(merchant_uid);
    }

    public void updateorder_count(int pd_id,int count){
        shopdao.updateorder_count(pd_id,count);
    }
}
