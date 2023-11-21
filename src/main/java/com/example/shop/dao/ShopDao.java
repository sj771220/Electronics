package com.example.shop.dao;

import com.example.shop.dto.Cart;
import com.example.shop.dto.OrderPd;
import com.example.shop.dto.Product;
import com.example.shop.dto.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ShopDao {

    public List<Product> getBest();

    public List<Product> topList();

    public List<Product> viewList();
    public List<Product> ratingList();

    public Product getProduct(int id);

    public List<Product> getCategoryList(String category);

    public int getCnt();

    public List<Product> findListPaging(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    public List<Product>getCategoryPaging(@Param("category")String category,@Param("startIndex") int startIndex, @Param("pageSize") int pageSize );

    public int getCategoryCnt(String category);
    public void insertCart(Long userid,int pd_count, int pd_id);

    public int aleadyInsert(Long userid,int pd_id);

    public void extraCart(int count, Long userid, int pd_id);

    public int getpdCnt(int pd_id);

    public Product showCart1(int id);

    public List<Cart> showCart2(Long id);

    public List<Product>searchKeyword(@Param("param") String param,@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    public int searchCnt(@Param("param") String param);

    public void removeCart(Long userid, int id);

    public void pluscart(Long userid, int id);

    public void minuscart(Long userid,int id);

    public void inventoryCart1(int pd_count,int id);

    public void inventoryCart2(int pd_count,int id);

    public UserVo getUserInfo(Long id);

    public void checkoutFinish1(String merchant_uid, Long userid, int amount, String delivery_status, String buyer_email, String buyer_address, String buyer_addDetail, int buyer_postcode, String buyer_phone, String buyer_name, LocalDate today);

    public void checkoutFinish2(String merchant_uid,int pd_id,int pd_count);

    public void checkoutFinish3(Long userid);

    public void updateView(int id);

    public String getpd_name(int pd_id);

    public List<OrderPd> getorderPd(String merchant_uid);

    public void updateorder_count(int pd_id,int count);
    }
