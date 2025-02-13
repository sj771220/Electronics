package com.example.shop.dao;

import com.example.shop.dto.OrderList;
import com.example.shop.dto.Review;
import com.example.shop.dto.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Mapper
@Repository
public interface UserDao {
    List<UserVo> getUserList(); // User 테이블 가져오기
    void insertUser(UserVo userVo); // 회원 가입
    UserVo getUserByUserid(String userid); // 회원 정보 가져오기
    UserVo getUserById(Long id);
    void deleteUser(Long id); // 회원 탈퇴

    public int checkId(String mid);
    public int checkmail(String mid);
    public int checktel(String mid);

    public void updatepwd(String pwd,Long loginid);
    public void makeUser(String name,String userid, String email, String password, String address, int postcode,String phone,String detail_address);

    public List<OrderList> getOrderList(Long userid,int startindex, int pagesize);

    public int gettotalorderct(Long userid);

    public void cancelOrder(String merchant_uid, Long userid, int amount, LocalDate canceldate);

    public OrderList getOrderbyUid(String merchant_uid);

    public void cancelOrder2(String type,String merchant_uid);

    public int getprice(int pd_id);

    public List<OrderList> getOrderSearch(Long userid,String orderdate,int startindex,int pagesize);

    public int getOrderSearchct(Long userid,String orderdate);

    public void insertreview(int pd_id,String merchant_uid,Long id,String review, int rate,LocalDate today);

    public int getratect(int pd_id);

    public int getidwithname(String pd_name);

    public int isaleadyrate(int pd_id,Long id);

    public int getPd_rate(int pd_id);

    public void updaterate(int rating,int pd_id);

    public List<Review> getSingleReview(int pd_id);

    public String getUseridWithLoginid(Long id);
    public String getUserpwWithLoginid(Long id);

    public void updateAddr(String addr, String addr_dtl, int postcode, Long loginid);


    public int aleadynews(String email);

    public void subcribenews(String email);
}