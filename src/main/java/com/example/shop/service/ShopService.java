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
import java.util.Map;

@Service
public class ShopService {

    private final ShopDao shopdao;
    @Autowired
    public ShopService(ShopDao shopDao){this.shopdao=shopDao;}


    public String calKrPrice(int totalPrice){
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);

        // 숫자를 형식화하여 문자열로 변환
        String formattedAmount = numberFormat.format(totalPrice);

        return formattedAmount;
    }

    public List<Product> calculPrice(List<Product>list){
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

    public List<Cart> calculPrice2(List<Cart>list){
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

    public List<Product> getBestList(){
        
        List<Product> list=shopdao.getBest();

        return calculPrice(list);
    
    }

    public List<Product> getListForRank(String which){

        List<Product> list=new ArrayList<>();

        switch (which){
            case "view": list=shopdao.viewList();
                break;
            case "top": list=shopdao.topList();
                break;
            case "rating": list=shopdao.ratingList();
                break;
            default:
                list=shopdao.viewList();
        }

        return calculPrice(list);
    }

    public Product getProduct(int id){
        return shopdao.getProduct(id);
    }

    public List<Product> getCategoryList(String category){
        List<Product>list=shopdao.getCategoryList(category);

        return calculPrice(list);

    }
    public int getCnt(){return shopdao.getCnt();}

    public List<Product> findListPaging(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize){
        List<Product> list=shopdao.findListPaging(startIndex,pageSize);

        return calculPrice(list);
    }
    public List<Product>getCategoryPaging(@Param("category")String category,@Param("startIndex") int startIndex, @Param("pageSize") int pageSize ){
        List<Product> list=shopdao.getCategoryPaging(category,startIndex,pageSize);
        return calculPrice(list);
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

    public Product getProductByCartID(int id){
        return shopdao.getProductByCartID(id);
    }

    public List<Cart> getCartListByUserID(Long id){
        List<Cart>list=shopdao.getCartListByUserID(id);

        return calculPrice2(list);
    }

    public List<Product>searchKeyword(@Param("param") String param,@Param("startIndex") int startIndex, @Param("pageSize") int pageSize){
        List<Product>list=calculPrice(shopdao.searchKeyword(param,startIndex,pageSize));
        return list;
    }

    public int searchCnt(@Param("param") String param){
        return shopdao.searchCnt(param);
    }

    public void removeCart(Long userid, int id){
        shopdao.removeCart(userid,id);
    }

    public void plustCart(Long userid, int id){
        shopdao.plustCart(userid,id);
    }

    public void minusCart(Long userid,int id){
        shopdao.minusCart(userid,id);
    }

    public void minusInventoryStock(int pd_count,int id){
        shopdao.minusInventoryStock(pd_count,id);
    }

    public void plusInventoryStock(int pd_count,int id){
        shopdao.plusInventoryStock(pd_count,id);
    }

    public UserVo getUserInfo(Long id){
           return shopdao.getUserInfo(id);
    }

    public void insertOrderList(Map<String,Object> paymentInfo){
        shopdao.insertOrderList(paymentInfo);
    }
    public void insertOrderPD(String merchant_uid,int pd_id,int pd_count){
        shopdao.insertOrderPD(merchant_uid,pd_id,pd_count);
    }

    public void deleteFromCart(Long userid){
        shopdao.deleteFromCart(userid);
    }
    public void updateView(int id){
        shopdao.updateView(id);
    }

    public String getProductNameByID(int pd_id){
        return shopdao.getProductNameByID(pd_id);
    }

    public List<OrderPd> getOrderPD(String merchant_uid){
        return shopdao.getOrderPD(merchant_uid);
    }

    public void updateorder_count(int pd_id,int count){
        shopdao.updateorder_count(pd_id,count);
    }

    public List<Map<String,Object>> SelectAllProductList(){return shopdao.SelectAllProductList();}

    public void UpdateProductImgTemp(Map<String,Object>paramMap){shopdao.UpdateProductImgTemp(paramMap);}
}
