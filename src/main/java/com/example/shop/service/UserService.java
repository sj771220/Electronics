package com.example.shop.service;

import com.example.shop.dao.UserDao;
import com.example.shop.dto.OrderList;
import com.example.shop.dto.Review;
import com.example.shop.dto.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
public class UserService {

    private final UserDao userDao;
    @Autowired
    public UserService(UserDao userDao){
        this.userDao = userDao;
    }


    public String calculKr(int a){

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);

        // 숫자를 형식화하여 문자열로 변환
        String formattedAmount = numberFormat.format(a);

        return formattedAmount;
    }

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<UserVo> getUserList() {
        return userDao.getUserList();
    }

    public UserVo getUserById(Long id) {
        return userDao.getUserById(id);
    }

    public UserVo getUserByUserid(String userid) {
        return userDao.getUserByUserid(userid);
    }

    public void signup(UserVo userVo) { // 회원 가입
        if (!userVo.getUserid().equals("") && !userVo.getEmail().equals("")) {
            // password는 암호화해서 DB에 저장
            userVo.setPassword(passwordEncoder.encode(userVo.getPassword()));
            userDao.insertUser(userVo);
        }
    }

    public void edit(UserVo userVo) { // 회원 정보 수정
        // password는 암호화해서 DB에 저장
        userVo.setPassword(passwordEncoder.encode(userVo.getPassword()));
        userDao.updateUser(userVo);
    }

    public void withdraw(Long id) { // 회원 탈퇴
        userDao.deleteUser(id);
    }

    public PasswordEncoder passwordEncoder() {
        return this.passwordEncoder;
    }

    public int checkId(String mid){
        return userDao.checkId(mid);
    }

    public int checkmail(String mid){
        return userDao.checkmail(mid);
    }

    public int checktel(String mid){
        return userDao.checktel(mid);
    }

    public void makeUser(String name,String userid, String email, String password, String address, int postcode,String phone,String detail_address){
        userDao.makeUser(name, userid, email, password, address, postcode, phone, detail_address);
    }

    public List<OrderList> getOrderList(Long userid,int startindex, int pagesize){

        List<OrderList> list= userDao.getOrderList(userid,startindex,pagesize);

        for(int i=0;i<list.size();i++){
            list.get(i).setKr_price(calculKr(list.get(i).getAmount()));
        }

        return list;
    }

    public int gettotalorderct(Long userid){
    return        userDao.gettotalorderct(userid);
    }

    public void cancelOrder(String merchant_uid, Long userid, int amount, LocalDate canceldate){
        userDao.cancelOrder(merchant_uid,userid,amount,canceldate);
    }
    public OrderList getOrderbyUid(String merchant_uid){
        return userDao.getOrderbyUid(merchant_uid);
    }

    public void cancelOrder2(String type,String merchant_uid){
        userDao.cancelOrder2(type,merchant_uid);
    }
    public int getprice(int pd_id){
        return  userDao.getprice(pd_id);
    }

    public List<OrderList> getOrderSearch(Long userid,String orderdate,int startindex,int pagesize){
          return  userDao.getOrderSearch(userid,orderdate,startindex,pagesize);
    }

    public int getOrderSearchct(Long userid,String orderdate){
        return userDao.getOrderSearchct(userid,orderdate);
    }

    public void insertreview(int pd_id,String merchant_uid,Long id,String review, int rate){
        userDao.insertreview(pd_id,merchant_uid,id,review,rate,LocalDate.now());
    }

    public int getratect(int pd_id){
        return userDao.getratect(pd_id);
    }
    public int getidwithname(String pd_name){
        return userDao.getidwithname(pd_name);
    }

    public int isaleadyrate(int pd_id,Long id){
        return userDao.isaleadyrate(pd_id,id);
    }

    public int getPd_rate(int pd_id){
        return userDao.getPd_rate(pd_id);
    }

    public void updaterate(int rating,int pd_id){
        userDao.updaterate(rating,pd_id);
    }

    public List<Review> getSingleReview(int pd_id){
        return userDao.getSingleReview(pd_id);
    }

    public String getUseridWithLoginid(Long id){
        return userDao.getUseridWithLoginid(id);
    }

    public String maskString(String input) {
        if (input != null && input.length() >= 5) {
            String prefix = input.substring(0, 3);
            String suffix = StringUtils.repeat("*", 2);
            return prefix + suffix;
        }
        return input;
    }

    public String getUserpwWithLoginid(Long id){
        return userDao.getUserpwWithLoginid(id);
    }

    public void updateAddr(String addr, String addr_dtl, int postcode, Long loginid){
        userDao.updateAddr(addr,addr_dtl,postcode,loginid);
    }

    public int aleadynews(String email){
        return userDao.aleadynews(email);
    }

    public void subcribenews(String email){
        userDao.subcribenews(email);
    }
}