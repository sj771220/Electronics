package com.example.shop.dao;

import com.example.shop.dto.Cart;
import com.example.shop.dto.OrderPd;
import com.example.shop.dto.Product;
import com.example.shop.dto.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    public Product getProductByCartID(int id);

    public List<Cart> getCartListByUserID(Long id);

    public List<Product>searchKeyword(@Param("param") String param,@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    public int searchCnt(@Param("param") String param);

    public void removeCart(Long userid, int id);

    public void plustCart(Long userid, int id);

    public void minusCart(Long userid,int id);

    public void minusInventoryStock(int pd_count,int id);

    public void plusInventoryStock(int pd_count,int id);

    public UserVo getUserInfo(Long id);

    public void insertOrderList(Map<String,Object> paymentInfo);

    public void insertOrderPD(String merchant_uid,int pd_id,int pd_count);

    public void deleteFromCart(Long userid);

    public void updateView(int id);

    public String getProductNameByID(int pd_id);

    public List<OrderPd> getOrderPD(String merchant_uid);

    public void updateorder_count(int pd_id,int count);

    public List<Map<String,Object>> SelectAllProductList();

    public void UpdateProductImgTemp(Map<String,Object>paramMap);
    }
